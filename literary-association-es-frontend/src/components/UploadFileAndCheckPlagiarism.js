import React, { useState } from "react";
import Header from "./Header";
import { uploadService } from "../services/upload-service";

import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { toast } from "react-toastify";

const UploadFileAndCheckPlagiarim = () => {
  const [fileUploaded, setFileUploaded] = useState(false);
  const [file, setFile] = useState();
  const [plagiarismFiles, setPlagiarismFiles] = useState([]);
  const downloadUrl = "https://localhost:9000/api/task/downloadFile?filePath=";

  const submitForm = async (event) => {
    event.preventDefault();
    try {
      const response = await uploadService.uploadFileForPlagiarism(file);
      console.log(response);
      setFileUploaded(true);
      toast.success("File uploaded", {
        hideProgressBar: true,
      });
      createPlagiarismLinks(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const createPlagiarismLinks = (response) => {
    let files = response.split("|");
    files.pop();
    setPlagiarismFiles(files);
    console.log(files);
  };

  const handleFileSelect = (e) => {
    setFile(e.target.files.item(0));
    console.log(file);
  };

  const reset = () => {
    setFileUploaded(false);
    setFile(null);
    setPlagiarismFiles([]);
  };

  return (
    <div>
      <Header />
      {!fileUploaded && (
        <div
          style={{
            width: "70%",
            margin: "auto",
            marginTop: "2em",
          }}
        >
          <h2> Upload File to Check for Plagiarism </h2>
          <Form
            style={{
              width: "40%",
              margin: "auto",
              marginTop: "3em",
            }}
            onSubmit={submitForm}
          >
            <Form.Group>
              <Form.File required onChange={(e) => handleFileSelect(e)} />
            </Form.Group>
            <Button
              variant="primary"
              type="submit"
              style={{ marginBottom: "1em", marginTop: "1em" }}
            >
              Submit
            </Button>
          </Form>
        </div>
      )}

      {fileUploaded && (
        <div
          style={{
            width: "70%",
            margin: "auto",
            marginTop: "2em",
          }}
        >
          <h2> Possible plagiated books </h2>
          {plagiarismFiles.map((file) => {
            return (
              <span>
                <p>
                  <a href={downloadUrl + file}> Download</a>
                </p>
              </span>
            );
          })}
          <Button
            style={{ borderRadius: "2em" }}
            variant="secondary"
            onClick={reset}
          >
            Check Again
          </Button>
        </div>
      )}
    </div>
  );
};

export default UploadFileAndCheckPlagiarim;
