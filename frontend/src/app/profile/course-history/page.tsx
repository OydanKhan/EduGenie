"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import {List, Typography, Card, Select, Button, DatePicker, Input, Row, Col} from "antd";
import Link from 'next/link';
import {SearchOutlined} from "@ant-design/icons";

const { Option } = Select;
const { RangePicker } = DatePicker;

interface Tag {
    tagId: string;
    name: string;
}

interface Course {
    noteId: string;
    title: string;
    topic: string;
    generatedDate: string;
    tags: Tag[];
}

const CourseHistory: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [tags, setTags] = useState<Tag[]>([]);
    const [selectedTag, setSelectedTag] = useState<string | null>(null);
    const [startDate, setStartDate] = useState<string | null>(null);
    const [endDate, setEndDate] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        fetchCourses();
        fetchTags();
    }, []);

    const fetchCourses = () => {
        const token = localStorage.getItem('token');
        axios.get(`http://localhost:8088/course-history`, {
            headers: { 'Authorization': `Bearer ${token}` },
            params: {
                tagName: selectedTag,
                startDate,
                endDate,
                searchTerm,
            }
        })
            .then(response => {
                setCourses(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching filtered courses:", error);
            });
    };

    const fetchTags = () => {
        const token = localStorage.getItem('token');
        axios.get(`http://localhost:8088/course-history/tags`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
            .then(response => {
                setTags(response?.data || []);
            })
            .catch(error => {
                console.error("Error fetching tags:", error);
            });
    };

    const handleFilter = () => {
        fetchCourses();
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            fetchCourses();
        }
    };

    if (loading) return <p>Loading...</p>;

    return (
        <div style={{ padding: "20px" }}>
            <Typography.Title level={3} style={{ marginBottom: "30px", textAlign: "center" }}>
                Course History
            </Typography.Title>

            <Input
                placeholder="Search course notes..."
                onChange={e => setSearchTerm(e.target.value)}
                onKeyPress={handleKeyPress}
                style={{ width: '300px', marginBottom: '20px' }}
                suffix={<SearchOutlined style={{ cursor: 'pointer' }} onClick={handleFilter} />}
            />

            <Row gutter={[16, 16]} style={{ marginBottom: '20px'}}>
                <Col>
                    <span style={{ marginRight: '5px' }}>Date:</span>
                    <RangePicker
                        style={{ width: '250px' }}
                        onChange={(dates) => {
                            if (dates) {
                                setStartDate(dates[0].toISOString());
                                setEndDate(dates[1].toISOString());
                            }
                        }}
                        onKeyPress={handleKeyPress}
                    />
                </Col>
                <Col>
                    <span style={{ marginRight: '5px' }}>Tag:</span>
                    <Select
                        placeholder="Select Tag"
                        onChange={setSelectedTag}
                        onKeyPress={handleKeyPress}
                        style={{ width: '200px' }}
                    >
                        {tags.map(tag => (
                            <Option key={tag.tagId} value={tag.name}>{tag.name}</Option>
                        ))}
                    </Select>
                </Col>
                <Col>
                    <Button onClick={handleFilter} type="primary" style={{ marginLeft: '10px', height: '40px' }}>Filter</Button>
                </Col>
            </Row>

            {courses.length === 0 ? (
                <p>No courses found.</p>
            ) : (
                <List
                    itemLayout="vertical"
                    dataSource={courses}
                    renderItem={(course: Course) => (
                        <List.Item>
                            <Link href={`/course/${course.noteId}`} passHref>
                                <div style={{
                                    borderBottom: "1px solid #e0e0e0",
                                    paddingBottom: "20px",
                                    marginBottom: "20px",
                                    cursor: "pointer"
                                }}>
                                    <Typography.Title level={5} style={{ marginBottom: '10px' }}>
                                        <strong>Topic:</strong> {course.title}
                                    </Typography.Title>

                                    <Typography.Paragraph>
                                        <strong>Tags:</strong>
                                        <div style={{ marginBottom: '10px' }}>
                                            {course.tags.map(tag => (
                                                <span key={tag.tagId} style={{
                                                    backgroundColor: "#e0e0e0",
                                                    borderRadius: "4px",
                                                    padding: "5px 10px",
                                                    marginRight: "10px",
                                                    marginBottom: "5px",
                                                    display: "inline-block"
                                                }}>
                                                {tag.name}
                                            </span>
                                            ))}
                                        </div>
                                    </Typography.Paragraph>

                                    <Typography.Paragraph>
                                        <small style={{ color: "#888888" }}>
                                            {new Date(course.generatedDate).toLocaleDateString()} {new Date(course.generatedDate).toLocaleTimeString()}
                                        </small>
                                    </Typography.Paragraph>
                                </div>
                            </Link>
                        </List.Item>
                    )}
                />
            )}
        </div>
    );

};

export default CourseHistory;
