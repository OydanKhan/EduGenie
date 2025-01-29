"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import { List, Typography, Card, DatePicker, Select, Input, Button, Row, Col  } from "antd";
import { SearchOutlined } from '@ant-design/icons';
import Link from 'next/link';

const { RangePicker } = DatePicker;
const { Option } = Select;

const PracticeHistory: React.FC = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [startDate, setStartDate] = useState<string | null>(null);
    const [endDate, setEndDate] = useState<string | null>(null);
    const [tags, setTags] = useState<string[]>([]);
    const [selectedTag, setSelectedTag] = useState<string | null>(null);
    const [minScore, setMinScore] = useState<number | null>(null);
    const [maxScore, setMaxScore] = useState<number | null>(null);
    const [searchTerm, setSearchTerm] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        fetchQuizzes();
        fetchTags();
    }, []);

    const fetchQuizzes = () => {
        const token = localStorage.getItem('token');
        axios.get(`http://localhost:8088/practise-history`, {
            headers: { 'Authorization': `Bearer ${token}` },
            params: {
                startDate,
                endDate,
                tagName: selectedTag,
                minScore,
                maxScore,
                searchTerm,
            }
        })
            .then(response => {
                setQuizzes(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching quizzes:", error);
            });
    };

    const fetchTags = () => {
        const token = localStorage.getItem('token');
        axios.get(`http://localhost:8088/practise-history/tags`, {
            headers: { 'Authorization': `Bearer ${token}` },
        })
            .then(response => {
                setTags(response?.data || []);
            })
            .catch(error => {
                console.error("Error fetching tags:", error);
                setLoading(false);
            });
    };

    const handleFilter = () => {
        fetchQuizzes();
    };

    const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            fetchQuizzes();
        }
    };

    if (loading) return <p>Loading...</p>;

    return (
        <div style={{ padding: "20px", minHeight: "100vh" }}>
            <Typography.Title level={3} style={{ textAlign: 'center', marginBottom: "30px" }}>Quiz History</Typography.Title>
            <Input
                placeholder="Search quizzes..."
                onChange={e => setSearchTerm(e.target.value)}
                onKeyPress={handleKeyPress}
                style={{ width: '300px', marginBottom: '20px' }}
                suffix={<SearchOutlined style={{ cursor: 'pointer' }} onClick={handleFilter} />}
            />
            <Row gutter={[16, 16]} style={{ marginBottom: '20px' }}>
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
                        {tags.map(tagName => (
                            <Option key={tagName} value={tagName}>{tagName}</Option>
                        ))}
                    </Select>
                </Col>

                <Col>
                    <span style={{ marginRight: '5px' }}>Min Score:</span>
                    <Input
                        type="number"
                        placeholder="0"
                        onChange={e => setMinScore(e.target.value ? parseInt(e.target.value) : null)}
                        onKeyPress={handleKeyPress}
                        style={{ width: '100px' }}
                    />
                </Col>

                <Col>
                    <span style={{ marginRight: '5px' }}>Max Score:</span>
                    <Input
                        type="number"
                        placeholder="10"
                        onChange={e => setMaxScore(e.target.value ? parseInt(e.target.value) : null)}
                        onKeyPress={handleKeyPress}
                        style={{ width: '100px' }}
                    />
                </Col>

                <Col>
                    <Button onClick={handleFilter} type="primary" style={{ marginLeft: '10px', height: '40px' }}>Filter</Button>
                </Col>
            </Row>

            {quizzes.length === 0 ? (
                <p>No quizzes found.</p>
            ) : (
                <Row gutter={[16, 16]} justify="start">
                    {quizzes.map((quiz) => {
                        const tagsQuery = quiz.tags.map(tag => tag.name).join(',');

                        return (
                            <Col key={quiz.quizId} xs={24} sm={12} md={8} lg={6}>
                                <Link href={`/practise/${quiz.quizId}?tags=${tagsQuery}`} passHref>
                                    <Card
                                        hoverable
                                        title={quiz.topic ? `Quiz on ${quiz.topic}` : `Quiz`}
                                        bordered={true}
                                        style={{
                                            borderRadius: "8px",
                                            padding: "20px",
                                            boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                                            backgroundColor: "#fff"
                                        }}
                                    >
                                        <Typography.Paragraph>
                                            <strong>Generated on:</strong> {new Date(quiz.generatedDate).toLocaleDateString()}
                                        </Typography.Paragraph>
                                        <Typography.Paragraph>
                                            <strong>Last attempted:</strong> {new Date(quiz.lastAttemptDate).toLocaleDateString()}
                                        </Typography.Paragraph>
                                        <Typography.Paragraph>
                                            <strong>Total Questions:</strong> {quiz.totalQuestions}
                                        </Typography.Paragraph>
                                        <Typography.Paragraph>
                                            <strong>Score:</strong> {quiz.score}
                                        </Typography.Paragraph>
                                        <Typography.Paragraph>
                                            <strong>Tags:</strong>
                                            <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                                                {quiz.tags.map(tag => (
                                                    <span
                                                        key={tag.tagId}
                                                        style={{
                                                            backgroundColor: '#f0f0f0',
                                                            borderRadius: '4px',
                                                            padding: '5px 10px',
                                                            marginRight: '10px',
                                                            marginBottom: '5px',
                                                        }}
                                                    >
                                                    {tag.name}
                                                </span>
                                                ))}
                                            </div>
                                        </Typography.Paragraph>
                                    </Card>
                                </Link>
                            </Col>
                        );
                    })}
                </Row>
            )}
        </div>
    );
};

export default PracticeHistory;
