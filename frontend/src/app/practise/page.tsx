"use client";

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import * as pdfjsLib from 'pdfjs-dist';
import mammoth from 'mammoth';
import { Tag } from 'antd';
import ClickToLookUp from "@/app/components/ClickToLookUp";
import { useRouter, useSearchParams } from 'next/navigation';


pdfjsLib.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjsLib.version}/pdf.worker.min.mjs`;

interface Question {
    questionId: string;
    questionText: string;
    options: string[];
    correctAnswer: string;
}

const QuizGenerator: React.FC = () => {
    const searchParams = useSearchParams(); 
    const [topic, setTopic] = useState<string>('');
    const [studyNotes, setStudyNotes] = useState<string>('');
    const [numberOfQuestions, setNumberOfQuestions] = useState<number>();
    const [quiz, setQuiz] = useState<Question[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [userAnswers, setUserAnswers] = useState<Map<string, string>>(new Map());
    const [score, setScore] = useState<number | null>(null);
    const [feedback, setFeedback] = useState<Map<string, boolean>>(new Map());
    const [file, setFile] = useState<File | null>(null);
    const [inputType, setInputType] = useState<'topic' | 'notes' | 'file' | null>(null);
    const [tags, setTags] = useState<string[]>([]);
    const colors = ['magenta', 'red', 'volcano', 'orange', 'gold', 'lime', 'green', 'cyan', 'blue', 'geekblue', 'purple'];
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [feedbackMessage, setFeedbackMessage] = useState<string | null>(null); // new state for feedback message

    useEffect(() => {
        const studyNotesParam = searchParams.get('studyNotes'); // Make sure this matches the parameter name used in practice
    
        console.log('studyNotesParam:', studyNotesParam);
    
        if (studyNotesParam) {
            setStudyNotes(decodeURIComponent(studyNotesParam));
            setInputType('notes');
        }
    }, [searchParams]);

    
    const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const uploadedFile = event.target.files?.[0];
        const maxSizeInBytes = 60000; // 60 KB size limit

        if (uploadedFile) {
            // if (uploadedFile.size > maxSizeInBytes) {
            //     setErrorMessage('File too large');
            //     setFile(null);
            //     return;
            // }

            setFile(uploadedFile);
            setErrorMessage(null); // Clear any previous error message

            const fileExtension = uploadedFile.name.split('.').pop()?.toLowerCase();

            if (fileExtension === 'txt') {
                const reader = new FileReader();
                reader.onload = (e) => {
                    if (e.target?.result) {
                        setStudyNotes(e.target.result as string);
                    }
                };
                reader.readAsText(uploadedFile);
            } else if (fileExtension === 'pdf') {
                const reader = new FileReader();
                reader.onload = async (e) => {
                    const typedArray = new Uint8Array(e.target?.result as ArrayBuffer);
                    const pdf = await pdfjsLib.getDocument(typedArray).promise;
                    let text = '';
                    for (let i = 0; i < pdf.numPages; i++) {
                        const page = await pdf.getPage(i + 1);
                        const content = await page.getTextContent();
                        const pageText = content.items.map(item => (item as any).str).join(' ');
                        text += pageText + '\n';
                    }
                    setStudyNotes(text);
                };
                reader.readAsArrayBuffer(uploadedFile);
            } else if (fileExtension === 'docx') {
                const reader = new FileReader();
                reader.onload = async (e) => {
                    const arrayBuffer = e.target?.result as ArrayBuffer;
                    const result = await mammoth.extractRawText({ arrayBuffer });
                    setStudyNotes(result.value);
                };
                reader.readAsArrayBuffer(uploadedFile);
            } else {
                alert('Unsupported file format. Please upload a .txt, .pdf, or .docx file.');
            }
        }
    };

    const handleGenerateQuiz = async () => {
        if (inputType === 'notes' && !studyNotes) {
            alert('Please provide study notes.');
            return;
        }
        if (inputType === 'topic' && !topic) {
            alert('Please provide a topic.');
            return;
        }
        if (inputType === 'file' && !file) {
            alert('Please upload a file.');
            return;
        }

        setLoading(true);
        try {
            const quizEndpoint = 'http://localhost:8088/quiz/generate';
            const tagsEndpoint = 'http://localhost:8088/quiz/tags';

            const quizResponse = await axios.post<Question[]>(quizEndpoint, {
                studyNotes: inputType === "notes" || inputType === "file" ? studyNotes : undefined,
                topic: inputType === "topic" ? topic : undefined,
                numberOfQuestions
            });

            setQuiz(quizResponse.data);
            setScore(null);
            setFeedback(new Map());
            const quizId = quizResponse.data?.[0]?.quiz.quizId;

            const tagsResponse = await axios.post(tagsEndpoint, {
                studyNotes: inputType === "notes" || inputType === "file" ? studyNotes : undefined,
                topic: inputType === "topic" ? topic : undefined,
                quizId
            });
            setTags(tagsResponse.data);
        } catch (error) {
            console.error('Error generating quiz or tags:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleAnswerChange = (questionId: string, answer: string) => {
        setUserAnswers(prevAnswers => new Map(prevAnswers).set(questionId, answer));
    };

    const handleSubmit = async () => {
        let correctCount = 0;
        const newFeedback = new Map<string, boolean>();
        const incorrectQuestions: string[] = []; // Array to collect incorrectly answered questions

        quiz.forEach((question) => {
            const userAnswer = userAnswers.get(question.questionId);
            const isCorrect = userAnswer === question.correctAnswer;
            newFeedback.set(question.questionId, isCorrect); // Set feedback for the question
            if (!isCorrect) {
                incorrectQuestions.push(question.questionText); // Add incorrect question to the array
            } else {
                correctCount++;
            }
        });

        setScore(correctCount);
        setFeedback(newFeedback); // Update the feedback state

        try {
            // Send the incorrect questions to the backend to get feedback
            const response = await axios.post('http://localhost:8088/quiz/feedback', incorrectQuestions);
            setFeedbackMessage(response.data); // Store feedback in state
        } catch (error) {
            console.error('Error submitting quiz:', error);
        }

        try {
            const response = await axios.post('http://localhost:8088/quiz/submit', null, {
                params: { score: correctCount }
            });

            console.log(response.data);
        } catch (error) {
            console.error('Error submitting quiz:', error);
        }
    };

    const scoreColor = score !== null ? (score === quiz.length ? 'green' : 'red') : 'black';

    return (
        <div style={{ fontFamily: 'Arial, sans-serif', margin: '20px', padding: '20px', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0,0,0,0.1)', backgroundColor: '#f8f9fa' }}>
            <h1 style={{ textAlign: 'center', marginBottom: '20px', color: '#343a40' }}>Quiz Generator</h1>
            <div style={{ marginBottom: '20px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div style={{ marginBottom: '10px', display: 'flex', justifyContent: 'space-around', width: '100%', maxWidth: '600px' }}>
                    <label>
                        <input
                            type="radio"
                            value="topic"
                            checked={inputType === 'topic'}
                            onChange={() => { setInputType('topic'); setTopic(''); setStudyNotes(''); setFile(null); }}
                        />
                        Enter Topic
                    </label>
                    <label>
                        <input
                            type="radio"
                            value="notes"
                            checked={inputType === 'notes'}
                            onChange={() => { setInputType('notes'); setTopic(''); setStudyNotes(''); setFile(null); }}
                        />
                        Enter Study Notes
                    </label>
                    <label>
                        <input
                            type="radio"
                            value="file"
                            checked={inputType === 'file'}
                            onChange={() => { setInputType('file'); setTopic(''); setStudyNotes(''); }}
                        />
                        Upload File
                    </label>
                </div>
                {inputType === 'topic' && (
                    <input
                        type="text"
                        value={topic}
                        onChange={(e) => setTopic(e.target.value)}
                        placeholder="Enter topic"
                        style={{ padding: '10px', fontSize: '16px', borderRadius: '4px', border: '1px solid #ccc', marginBottom: '10px', width: '100%', maxWidth: '400px' }}
                    />
                )}
                {inputType === 'notes' && (
                    <textarea
                        value={studyNotes}
                        onChange={(e) => setStudyNotes(e.target.value)}
                        placeholder="Paste your study notes here..."
                        style={{ padding: '10px', fontSize: '16px', borderRadius: '4px', border: '1px solid #ccc', marginBottom: '10px', width: '100%', maxWidth: '400px', minHeight: '100px' }}
                    />
                )}
                {inputType === 'file' && (
                    <input
                        type="file"
                        accept=".txt,.pdf,.docx"
                        onChange={handleFileChange}
                        style={{ marginBottom: '10px', width: '100%', maxWidth: '400px' }}
                    />
                )}
                {errorMessage && (
                    <div style={{
                        color: '#721c24',
                        backgroundColor: '#f8d7da',
                        border: '1px solid #f5c6cb',
                        padding: '10px',
                        borderRadius: '4px',
                        marginBottom: '10px',
                        width: '100%',
                        maxWidth: '400px',
                        textAlign: 'center'
                    }}>
                        {errorMessage}
                    </div>
                )}
                <input
                    type="number"
                    value={numberOfQuestions === undefined ? '' : numberOfQuestions}
                    onChange={(e) => setNumberOfQuestions(e.target.value === '' ? undefined : Number(e.target.value))}
                    placeholder="Select number of questions"
                    style={{ padding: '10px', fontSize: '16px', borderRadius: '4px', border: '1px solid #ccc', marginBottom: '10px', width: '100%', maxWidth: '400px' }}
                />
                <button
                    onClick={handleGenerateQuiz}
                    disabled={loading}
                    style={{
                        padding: '10px 20px',
                        fontSize: '16px',
                        borderRadius: '4px',
                        border: 'none',
                        backgroundColor: '#007bff',
                        color: '#fff',
                        cursor: 'pointer',
                        transition: 'background-color 0.3s ease',
                        marginBottom: '20px'
                    }}
                >
                    {loading ? 'Generating...' : 'Generate Quiz'}
                </button>
            </div>

            {quiz.length > 0 && (
                <div>
                    {quiz.map((question, index) => {
                        const isCorrect = feedback.get(question.questionId);
                        const questionNumber = index + 1;

                        return (
                            <div key={question.questionId} style={{ marginBottom: '20px', padding: '10px', borderRadius: '8px', backgroundColor: isCorrect === true ? '#d4edda' : isCorrect === false ? '#f8d7da' : 'transparent' }}>
                                <h3 style={{ marginBottom: '10px' }}>{questionNumber}. <ClickToLookUp>{question.questionText}</ClickToLookUp></h3>  
                                <ul style={{ listStyleType: 'none', padding: 0 }}>
                                    {question.options.map((option, i) => (
                                        <li key={i} style={{ marginBottom: '10px', display: 'flex', alignItems: 'center' }}>
                                            <input
                                                type="radio"
                                                name={`question-${question.questionId}`}
                                                value={option}
                                                checked={userAnswers.get(question.questionId) === option}
                                                onChange={() => handleAnswerChange(question.questionId, option)}
                                                style={{ marginRight: '10px' }}
                                            />
                                            <ClickToLookUp>{option}</ClickToLookUp>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        );
                    })}
                    <div style={{ textAlign: 'center' }}>
                        <button
                            onClick={handleSubmit}
                            style={{ padding: '10px 20px', fontSize: '16px', borderRadius: '4px', border: 'none', backgroundColor: '#007bff', color: '#fff', cursor: 'pointer' }}
                        >
                            Submit
                        </button>
                    </div>
                </div>
            )}
            {score !== null && (
                <div style={{ marginTop: '20px', textAlign: 'center', color: scoreColor }}>
                    <h2>Score</h2>
                    <p>You got {score} out of {quiz.length} correct.</p>
                </div>
            )}


            {feedbackMessage && ( // Conditionally render feedback section if feedback exists
                <div style={{ marginTop: '20px', padding: '15px', borderRadius: '8px', backgroundColor: '#f1f1f1', color: '#333' }}>
                    <h3>Feedback on Incorrect Answers:</h3>
                    <p>{feedbackMessage}</p>
                </div>
            )}

            {tags.length > 0 && (
                <div style={{ marginTop: '20px' }}>
                    <h2 style={{ color: '#343a40' }}>Suggested Tags</h2>
                    {tags.map((tag, index) => (
                        <Tag key={index} color={colors[index % colors.length]} style={{ marginBottom: '8px' }}>{tag}</Tag>
                    ))}
                </div>
            )}
        </div>
    );
};

export default QuizGenerator;