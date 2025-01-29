package com.usyd.edugenie.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.mockito.Mockito.*;

class DownloadCourseControllerTest {

    @InjectMocks
    private DownloadCourseController downloadCourseController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private static final String FILE_NAME = "testFile.pdf";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDownloadFileExists() throws Exception {
        // Create a temporary file to simulate existence
        File file = new File("generated_pdfs/" + FILE_NAME);
        file.getParentFile().mkdirs();
        file.createNewFile();

        // Write some dummy data to the file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("Sample content".getBytes());
        }

        // Mock response output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new DelegatingServletOutputStream(outputStream);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        // Call the download method
        downloadCourseController.download(FILE_NAME, request, response);

        // Verify headers and content type
        verify(response).setContentType("application/pdf");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"" + FILE_NAME + "\"");
        verify(response).setContentLengthLong(file.length());

        // Cleanup
        file.delete();
    }

    // Helper class to delegate to ByteArrayOutputStream in tests
    private static class DelegatingServletOutputStream extends ServletOutputStream {
        private final OutputStream targetStream;

        public DelegatingServletOutputStream(OutputStream targetStream) {
            this.targetStream = targetStream;
        }

        @Override
        public void write(int b) throws IOException {
            targetStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            targetStream.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // No implementation needed for this mock
        }
    }
}



