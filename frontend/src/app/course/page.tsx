"use client";

import React, { useState, useEffect} from 'react';
import axios from 'axios';
import styles from './page.module.css';
import {
    MDBContainer,
    MDBInputGroup,
} from 'mdb-react-ui-kit';
import { SearchOutlined } from '@ant-design/icons';
import { Button, Flex, Tag, Card, Space } from 'antd';
import jsPDF from 'jspdf';
import { saveAs } from 'file-saver';
import { useRouter } from "next/navigation";


const CourseGenerator: React.FC = () => {
    const [topic, setTopic] = useState(''); // current topic
    const [inputValue, setInputValue] = useState(''); // the content in the search box
    const [course, setCourse] = useState(''); // generated course
    const [tags, setTags] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [placeholder, setPlaceholder] = useState("Search what you want to learn like \"machine learning\"");
    const colors = ['magenta', 'red', 'volcano', 'orange', 'gold', 'lime', 'green', 'cyan', 'blue', 'geekblue', 'purple'];

    const router = useRouter();

//  // Add a useEffect hook to make API request when 'topic' changes
//  useEffect(() => {
//     const handleGenerateCourse = async () => {
//         if (!topic) return;  // Skip if topic is empty
//         setLoading(true);
//         try {
//             const courseResponse = await axios.get('http://localhost:8088/learn/course', { params: { topic } });
//             const courseId = courseResponse.data.noteId;
//             const tagsResponse = await axios.get('http://localhost:8088/learn/tags', { params: { topic, courseId } });

//             setCourse(courseResponse.data.content);
//             setTags(tagsResponse.data);

//             setInputValue(''); // Clear the input
//             setPlaceholder("Input your problem");
//         } catch (error) {
//             console.error('Error generating course or tags:', error);
//             alert(`Error: ${error.response.data.message}`);
//         } finally {
//             setLoading(false);
//         }
//     };

//     handleGenerateCourse();  // Call the API when topic updates
// }, [topic]);  // This effect runs whenever 'topic' changes





const handleGenerateCourse = async () => {
    if (!inputValue) return;  // Skip if input is empty
    setLoading(true);
    try {
        const courseResponse = await axios.get('http://localhost:8088/learn/course', { params: { topic: inputValue } });
        const courseId = courseResponse.data.noteId;
        const tagsResponse = await axios.get('http://localhost:8088/learn/tags', { params: { topic: inputValue, courseId } });

        setCourse(courseResponse.data.content);
        setTags(tagsResponse.data);

        setInputValue(''); // Clear the input
        setPlaceholder("Input your problem");
    } catch (error) {
        console.error('出错了出错了');
        console.error('Error generating course or tags:', error);
        alert(`Error: ${error.response.data.message}`);
    } finally {
        setLoading(false);
    }
};

// Call `handleGenerateCourse` directly when clicking search button
const handleSearchClick = () => {
    handleGenerateCourse();
};



// const handleSearchClick = () => {
//     setTopic(inputValue);  // Update 'topic' state based on the input value
// };


    const learnOthers = async () => {
        window.location.reload();
    };

    // const download = async () => {
    //     const fileName = `${topic || 'course'}.txt`; // 设置文件名
    //     const content = `Title: ${topic}\n\nContent:\n${course}`; // 设置文件内容
    //     const blob = new Blob([content], { type: 'text/plain' }); // 创建 Blob 对象
    //     const url = URL.createObjectURL(blob); // 创建文件下载链接

    //     const a = document.createElement('a'); // 创建链接元素
    //     a.href = url; // 设置链接的 URL
    //     a.download = fileName; // 设置下载的文件名
    //     document.body.appendChild(a); // 将链接添加到文档
    //     a.click(); // 自动点击链接以触发下载
    //     document.body.removeChild(a); // 移除链接
    //     URL.revokeObjectURL(url); // 释放 URL 对象
    //  };


    const download = async (fileType: string) => {
        const fileName = `${topic || 'course'}.${fileType}`;
        const content = `${topic}\n\n${course}`;

        if (fileType === 'txt') {
            const blob = new Blob([content], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);

        } else if (fileType === 'pdf') {
            const doc = new jsPDF();
            // 使用 splitTextToSize 方法将文本拆分为适合页面的行
            const lines = doc.splitTextToSize(content, 190); // 190 是每行的最大宽度
            doc.text(lines, 10, 10); // 从坐标 (10, 10) 开始绘制
            doc.save(fileName);

        } else if (fileType === 'docx') {
            const docxContent = `Title: ${topic}\n\nContent:\n${course}`;
            const blob = new Blob([docxContent], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' });
            saveAs(blob, fileName);

        } else {
            alert('Unsupported file format. Please select .txt, .pdf, or .docx format.');
        }
    };

    const practice = async () => {
        const encodedCourse = encodeURIComponent(course);
        // Use the correct parameter name 'studyNotes'
        window.location.href = `/practise?studyNotes=${encodedCourse}`;
    };

    return (
        <div>
            <div className={styles.courseheader}>
                <h1>Follow me to learn</h1>
            </div>

            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <MDBInputGroup className={styles.searchContainer}>
                    <input
                        type="text"
                        className={styles.searchInput}
                        value={inputValue} // use the value of input as topic
                        onChange={(e) => setInputValue(e.target.value)} // update inputValue
                        placeholder={placeholder} 
                    />
                    {/* <Button type="primary" icon={<SearchOutlined />} onClick={() => { setTopic(inputValue); handleSearchClick; }} disabled={loading} className={styles.searchButton}>
                    </Button> */}

                                        <Button type="primary" icon={<SearchOutlined />} onClick={() => { setTopic(inputValue); handleSearchClick(); }} disabled={loading} className={styles.searchButton}>
                    </Button>
                </MDBInputGroup>
            </div>

            {course && (
                <div>
                    <MDBContainer>
                        <div style={{ paddingTop: '5px', marginBottom: '10px' }}>
                            <h2>Result</h2>
                        </div>

                        <Flex gap="4px 4px" wrap>
                            {tags.map((tag, index) => (
                                <Tag key={index} color={colors[index % colors.length]}>{tag.name.trim()}</Tag>
                            ))}


                        </Flex>

                        <Space direction="vertical" size="middle" style={{ display: 'flex', paddingTop: '5px' }}>
                            <Card title={topic} size="small">
                                <p>{course}</p>
                            </Card>
                        </Space>

                        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '10px' }}>
                            <Button type="primary" onClick={learnOthers}>Learn others</Button>
                            <Button type="primary" onClick={() => download('pdf')}>Download</Button>
                            <Button type="primary" onClick={practice}>Practice</Button>
                        </div>
                    </MDBContainer>
                </div>
            )}
        </div>
    );
};

export default CourseGenerator;













