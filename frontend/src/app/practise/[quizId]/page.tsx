"use client";

import React, { useEffect, useState } from "react";
import { usePathname, useSearchParams } from "next/navigation";
import axios from "axios";

interface Question {
    questionId: string;
    questionText: string;
    options: string[];
    correctAnswer: string;
}

interface Quiz {
    quizId: string;
    totalQuestions: number;
    score: number;
    lastAttemptDate: string;
    topic: string;
}

const QuizDetail: React.FC = () => {
    const [quizData, setQuizData] = useState<Quiz | null>(null);
    const [quizQuestions, setQuizQuestions] = useState<Question[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [userAnswers, setUserAnswers] = useState<Map<string, string>>(new Map());
    const [score, setScore] = useState<number | null>(null);
    const [feedback, setFeedback] = useState<Map<string, boolean>>(new Map());
    const [feedbackMessage, setFeedbackMessage] = useState<string | null>(null);
    const [quizTags, setQuizTags] = useState<string[]>([]);
    const pathname = usePathname();
    const quizId = pathname.split("/").pop();
    const searchParams = useSearchParams();

    // Fetch the quiz when the component mounts
    useEffect(() => {
        setLoading(true);

        axios
            .get(`http://localhost:8088/quiz/${quizId}`)
            .then((response) => {
                const { quiz, questions } = response.data;
                setQuizData(quiz);
                setQuizQuestions(questions);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching quiz data:", error);
                setLoading(false);
            });
    }, [quizId]);

    useEffect(() => {
        const tagsParam = searchParams.get('tags');
        if (tagsParam) {
            const tagsArray = tagsParam.split(',');
            setQuizTags(tagsArray);
        }
    }, [searchParams]);

    // Handle the user selecting an answer
    const handleAnswerChange = (questionId: string, answer: string) => {
        setUserAnswers((prevAnswers) => new Map(prevAnswers).set(questionId, answer));
    };

    // Handle the submission of the quiz
    const handleSubmit = async () => {
        let correctCount = 0;
        const newFeedback = new Map<string, boolean>();
        const incorrectQuestions: string[] = []; // Array to collect incorrect questions

        // Calculate the number of correct answers and collect incorrect ones
        quizQuestions.forEach((question) => {
            const userAnswer = userAnswers.get(question.questionId);
            if (userAnswer === question.correctAnswer) {
                correctCount++;
                newFeedback.set(question.questionId, true); // Correct answer
            } else {
                newFeedback.set(question.questionId, false); // Incorrect answer
                incorrectQuestions.push(question.questionText); // Collect the incorrect question
            }
        });

        setScore(correctCount); // Update the score
        setFeedback(newFeedback); // Update the feedback

        // Submit only the score to the backend
        try {
            await axios.post("http://localhost:8088/quiz/submit", null, {
                params: { score: correctCount },
            });
            console.log("Quiz submitted successfully.");
        } catch (error) {
            console.error("Error submitting quiz:", error);
        }

        // Send the incorrect questions to the backend for feedback
        try {
            const response = await axios.post('http://localhost:8088/quiz/feedback', incorrectQuestions);
            setFeedbackMessage(response.data); // Update feedback message state with response from backend
        } catch (error) {
            console.error('Error fetching feedback:', error);
        }
    };

    if (loading) {
        return <p>Loading quiz...</p>;
    }

    const scoreColor = score !== null ? (score === quizQuestions.length ? "green" : "red") : "black";

    return (
        <div
            style={{
                fontFamily: "Arial, sans-serif",
                margin: "20px",
                padding: "20px",
                borderRadius: "8px",
                boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                backgroundColor: "#f8f9fa",
                maxWidth: "800px",
                marginLeft: "auto",
                marginRight: "auto",
            }}
        >
            <h1 style={{ textAlign: "center", marginBottom: "20px", color: "#343a40" }}>Quiz</h1>

            {quizData && (
                <div
                    style={{
                        backgroundColor: "#e9ecef",
                        padding: "15px",
                        borderRadius: "8px",
                        marginBottom: "20px",
                        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                    }}
                >
                    <p style={{ fontSize: "18px", fontWeight: "bold" }}>Topic: {quizData.topic}</p>
                    <p style={{ fontSize: "16px", margin: "5px 0" }}>
                        Last Attempt Date:{" "}
                        {quizData?.lastAttemptDate
                            ? new Date(quizData.lastAttemptDate).toLocaleString(undefined, {
                                year: "numeric",
                                month: "long",
                                day: "numeric",
                                hour: "2-digit",
                                minute: "2-digit",
                            })
                            : "Never attempted"}
                    </p>
                    <p style={{ fontSize: "16px", margin: "5px 0" }}>Last Score: {quizData.score}</p>
                </div>
            )}

            {quizTags && quizTags.length > 0 && (
                <div style={{ marginBottom: '20px' }}>
                    <h3>Tags:</h3>
                    <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                        {quizTags.map((tag, index) => (
                            <span
                                key={index}
                                style={{
                                    backgroundColor: '#f0f0f0',
                                    borderRadius: '4px',
                                    padding: '5px 10px',
                                    marginRight: '10px',
                                    marginBottom: '5px',
                                }}
                            >
                    {tag}
                </span>
                        ))}
                    </div>
                </div>
            )}

            {quizData && quizQuestions.length > 0 && (
                <div>
                    {quizQuestions.map((question, index) => {
                        const isCorrect = feedback.get(question.questionId);
                        const questionNumber = index + 1;

                        return (
                            <div
                                key={question.questionId}
                                style={{
                                    marginBottom: "20px",
                                    padding: "10px",
                                    borderRadius: "8px",
                                    backgroundColor:
                                        isCorrect === true
                                            ? "#d4edda"
                                            : isCorrect === false
                                                ? "#f8d7da"
                                                : "transparent",
                                    boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                                }}
                            >
                                <h3 style={{ marginBottom: "10px", fontSize: "18px" }}>
                                    {questionNumber}. {question.questionText}
                                </h3>
                                <ul style={{ listStyleType: "none", padding: 0 }}>
                                    {question.options.map((option, i) => (
                                        <li
                                            key={i}
                                            style={{
                                                marginBottom: "10px",
                                                display: "flex",
                                                alignItems: "center",
                                            }}
                                        >
                                            <input
                                                type="radio"
                                                name={`question-${question.questionId}`}
                                                value={option}
                                                checked={userAnswers.get(question.questionId) === option}
                                                onChange={() => handleAnswerChange(question.questionId, option)}
                                                style={{ marginRight: "10px" }}
                                            />
                                            {option}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        );
                    })}
                    <div style={{ textAlign: "center" }}>
                        <button
                            onClick={handleSubmit}
                            style={{
                                padding: "10px 20px",
                                fontSize: "16px",
                                borderRadius: "4px",
                                border: "none",
                                backgroundColor: "#007bff",
                                color: "#fff",
                                cursor: "pointer",
                            }}
                        >
                            Submit
                        </button>
                    </div>
                </div>
            )}

            {score !== null && (
                <div style={{ marginTop: "20px", textAlign: "center", color: scoreColor }}>
                    <h2>Score</h2>
                    <p>
                        You got {score} out of {quizQuestions.length} correct.
                    </p>
                </div>
            )}

            {feedbackMessage && (
                <div style={{ marginTop: '20px', padding: '15px', borderRadius: '8px', backgroundColor: '#f1f1f1', color: '#333' }}>
                    <h3>Feedback on Incorrect Answers:</h3>
                    <p>{feedbackMessage}</p>
                </div>
            )}
        </div>
    );
};

export default QuizDetail;
