"use client";

import React, { useEffect, useState } from "react";
import { usePathname } from "next/navigation";
import axios from "axios";

interface StudyNote {
    noteId: string;
    title: string;
    topic: string;
    content: string;
    generatedDate: string;
    downloadFile: string;
    tags: string[];
}

const NoteDetail: React.FC = () => {
    const [noteData, setNoteData] = useState<StudyNote | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const pathname = usePathname();
    const noteId = pathname.split("/").pop(); // Extract the noteId from the URL

    // Fetch the note when the component mounts
    useEffect(() => {
        setLoading(true);

        axios
            .get(`http://localhost:8088/learn/${noteId}`)
            .then((response) => {
                setNoteData(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Error fetching note data:", error);
                setLoading(false);
            });
    }, [noteId]);








    
    const handleDownload = async () => {
        try {
            const response = await axios.get(noteData.downloadFile, {
                responseType: "blob", 
            });
    
            // create a url
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
            const fileName = noteData.topic + '.pdf'; 
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", fileName); // set the name of download file
            document.body.appendChild(link);
            link.click(); 
            link.remove(); 
        } catch (error) {
            console.error("Errors during download:", error);
            alert("Errors during download");
        }
    };
    














    if (loading) {
        return <p>Loading note...</p>;
    }

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
            <h1 style={{ textAlign: "center", marginBottom: "20px", color: "#343a40" }}>Study Note</h1>

            {noteData && (
                <div
                    style={{
                        backgroundColor: "#e9ecef",
                        padding: "15px",
                        borderRadius: "8px",
                        marginBottom: "20px",
                        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                    }}
                >
                    <p style={{ fontSize: "18px", fontWeight: "bold" }}>Title: {noteData.title}</p>
                    <p style={{ fontSize: "16px", margin: "5px 0" }}>Generated Date: {noteData?.generatedDate ? new Date(noteData.generatedDate).toLocaleString(undefined, {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    }) : 'Unknown'}</p>


                </div>
            )}

            {noteData && (
                <p style={{ fontSize: "16px", margin: "5px 0" }}>Tags:
                    {noteData.tags.length > 0 ? (
                        noteData.tags.map((tag, index) => (
                            <span key={index} style={{
                                backgroundColor: "#e2e3e5",
                                borderRadius: "4px",
                                padding: "5px 10px",
                                margin: "5px",
                                color: "#495057",
                            }}>
                                        {tag}
                                    </span>
                        ))
                    ) : (
                        <span>No tags available</span>
                    )}
                </p>
            )}

            {noteData && (
                <div>
                    <h2 style={{ color: "#007bff", marginTop: "20px" }}>Content</h2>
                    <p style={{ fontSize: "16px", lineHeight: "1.6", margin: "20px 0" }}>{noteData.content}</p>

                    {noteData.downloadFile && (
                        <button
                            onClick={handleDownload}
                            style={{
                                padding: "10px 20px",
                                fontSize: "16px",
                                borderRadius: "4px",
                                border: "none",
                                backgroundColor: "#28a745",
                                color: "#fff",
                                textDecoration: "none",
                                display: "inline-block",
                                marginTop: "10px",
                            }}
                        >
                            Download Attachment
                        </button>
                    )}
                </div>
            )}
        </div>
    );
};

export default NoteDetail;
