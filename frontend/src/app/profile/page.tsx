"use client";
import React, { useEffect, useState } from "react";
import { Card, Avatar, Button, Col, Row, Menu } from "antd";
import ReactECharts from "echarts-for-react";
import axios from "axios";
import styles from "./page.module.css";
import CourseHistory from "./course-history/page";
import PractiseHistory from "./practise-history/page";
import UserSettings from "./settings";

interface User {
  email: string;
  firstName: string;
  lastName: string;
  avatar: string;
}

interface TagScoreItem {
  tag: string;
  avgScore: number;
}

const Profile: React.FC = () => {
  const [userInfo, setUserInfo] = useState<User | null>(null);
  const [tagScores, setTagScores] = useState<TagScoreItem[]>([
    { tag: "", avgScore: 0 },
  ]);

  const keys = {
    analysis: "Study Analysis",
    course: "Course History",
    practise: "Practise History",
    settings: "Settings",
  };
  const [contentKey, setContentKey] = useState(keys.analysis);

  const getUserInfo = () => {
    axios
      .get("http://localhost:8088/user/info")
      .then((res) => {
        if (!res.data) {
          return;
        }
        setUserInfo(res.data);
      })
      .catch((reason) => {
        console.log("fetch user info fail:", reason);
      });
  };

  const getUserStudyAnalysis = () => {
    axios
      .get("http://localhost:8088/quiz/tagscores")
      .then((res) => {
        if (!(res?.data?.length > 0)) {
          return;
        }
        setTagScores(res.data);
      })
      .catch((reason) => {
        console.log("fetch tag scores fail:", reason);
      });
  };

  useEffect(() => {
    getUserInfo();
    getUserStudyAnalysis();
  }, []);

  const getOption = () => {
    const tags = tagScores.map((i) => {
      return { text: i.tag, max: 100 };
    });
    const scores = tagScores.map((i) => i.avgScore);

    return {
      color: ["#FFE434"],
      radar: [
        {
          indicator: tags,
          center: ["50%", "50%"],
          radius: 200,
          startAngle: 90,
          splitNumber: 4,
          shape: "circle",
          axisName: {
            color: "#428BD4",
          },
          splitArea: {
            areaStyle: {
              color: ["#77EADF", "#26C3BE", "#64AFE9", "#428BD4"],
              shadowColor: "rgba(0, 0, 0, 0.2)",
              shadowBlur: 10,
            },
          },
          axisLine: {
            lineStyle: {
              color: "rgba(211, 253, 250, 0.8)",
            },
          },
          splitLine: {
            lineStyle: {
              color: "rgba(211, 253, 250, 0.8)",
            },
          },
        },
      ],
      series: [
        {
          type: "radar",
          data: [
            {
              value: scores,
              areaStyle: {
                color: "rgba(255, 228, 52, 0.6)",
              },
            },
          ],
        },
      ],
    };
  };

  const onClickMenuItem = (e: any) => {
    console.log(e);
    setContentKey(e.key);
  };

  const items = [
    {
      key: keys.analysis,
      label: keys.analysis,
    },
    {
      key: keys.practise,
      label: keys.practise,
    },
    {
      key: keys.course,
      label: keys.course,
    },
    {
      key: keys.settings,
      label: keys.settings,
    },
  ];

  return (
    <Row gutter={24}>
      <Col span={6}>
        <Card title="Personal Information">
          <div className={styles.infoWrap}>
            <Avatar src={userInfo?.avatar} size={90}>{userInfo?.firstName?.[0]}</Avatar>
            <h1 className={styles.userName}>
              {userInfo?.firstName} {userInfo?.lastName}
            </h1>
            <div className={styles.userEmail}>{userInfo?.email}</div>
            <Menu
              onClick={onClickMenuItem}
              defaultSelectedKeys={[keys.analysis]}
              mode="vertical"
              items={items}
              style={{ marginTop: "24px" }}
            />
          </div>
        </Card>
      </Col>

      <Col span={18}>
        {contentKey == keys.analysis && (
          <Card title="Study Analysis">
            <ReactECharts style={{ height: "500px" }} option={getOption()} />
            <h3>
              This radar chart visualises your performance across all learning
              domains. Each axis reflects quiz-based scores for a specific area.
            </h3>
          </Card>
        )}
        {contentKey == keys.course && <CourseHistory />}
        {contentKey == keys.practise && <PractiseHistory />}
        {contentKey == keys.settings && (
          <Card title="User Settings">
            <UserSettings userInfo={userInfo} onSuccess={getUserInfo} />
          </Card>
        )}
      </Col>
    </Row>
  );
};

export default Profile;
