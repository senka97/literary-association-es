import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import { searchService } from "../services/search-service";
import { toast } from "react-toastify";

const PayTest = () => {
  const [loggedIn, setLoggedIn] = useState(
    localStorage.getItem("token") !== "null" &&
      localStorage.getItem("token") !== null
  );
  const [role, setRole] = useState(localStorage.getItem("role"));

  /*const membershipFeePayment = async () => {
    console.log("Membership fee payment started...");
    try {
      const response = await writerService.membershipFeePayment();
      toast.success("Membership Fee Successfully paid!", {
        hideProgressBar: true,
      });
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };*/

  const indexBooks = async () => {
    console.log("Indexing started...");
    try {
      const response = await searchService.indexBooks();
      toast.success(response, {
        hideProgressBar: true,
      });
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const indexBetaReaders = async () => {
    console.log("Indexing started...");
    try {
      const response = await searchService.indexBetaReaders();
      toast.success(response, {
        hideProgressBar: true,
      });
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  return (
    <div>
      {/*{loggedIn && role === "ROLE_WRITER" && (
        <Button variant="info" onClick={membershipFeePayment}>
          Membership Fee Payment
        </Button>
      )}*/}
      <Button variant="dark" onClick={indexBooks}>
        Index Books
      </Button>

      <Button variant="dark" onClick={indexBetaReaders}>
        Index Beta Readers
      </Button>
    </div>
  );
};

export default PayTest;
