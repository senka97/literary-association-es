import React, { useState } from "react";
import { useFormik } from "formik";
import Header from "./Header";
import { bookService } from "../services/book-service";
import { shoppingCartService } from "../services/shopping-cart-service";
import { searchService } from "../services/search-service";
import { toast } from "react-toastify";
import { AMOUNT } from "../constants";
import Select from "react-select";
import { Button, Modal } from "react-bootstrap";
import * as Yup from "yup";

const SearchBooks = () => {
  const [books, setBooks] = useState([]);
  const [searchDone, setSearchDone] = useState(false);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const downloadUrl = "https://localhost:9000/api/task/downloadFile?filePath=";
  const [selectedAmount, setSelectedAmount] = useState({
    value: 1,
    label: "1",
  });

  const addToCart = async (book, bookAmount) => {
    let currentUserId = localStorage.getItem("currentUserId");
    if (currentUserId === null || currentUserId === undefined) {
      toast.error("You have to login first.", {
        hideProgressBar: true,
      });
      return;
    } else {
      let role = localStorage.getItem("role");
      if (role !== "ROLE_READER") {
        toast.error("Only readers can buy books.", {
          hideProgressBar: true,
        });
        return;
      }
    }
    shoppingCartService.addBookToCart(book, bookAmount);
  };

  const handleCancel = () => {
    setShowDetails(false);
    setSelectedAmount({ value: 1, label: "1" });
  };

  const seeBookDetails = async (bookId) => {
    try {
      const response = await bookService.getBookDetails(bookId);
      console.log(response);
      setBookToShow(response);
      setSelectedAmount({ value: 1, label: "1" });
      setShowDetails(true);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const findHighlights = (highlights) => {
    return highlights.map((h) => (
      <p dangerouslySetInnerHTML={{ __html: h.highlight }} />
    ));
  };

  const formik = useFormik({
    initialValues: {
      field: "",
      value: "",
      phrase: false,
    },
    validationSchema: Yup.object().shape({
      field: Yup.string().required("Choose which field you are searching"),
      value: Yup.string().required("Search value is required"),
    }),
    onSubmit: async (values, { resetForm }) => {
      try {
        setSearchDone(false);
        const response = await searchService.simpleSearch(values);
        console.log(response);
        resetForm();
        setBooks(response);
        if (response.length === 0) {
          toast.info("No books found. Try again with different arguments.", {
            hideProgressBar: true,
          });
        } else setSearchDone(true);
      } catch (error) {
        if (error.response) {
          console.log("Error: " + JSON.stringify(error.response));
        }
        toast.error(error.response ? error.response.data : error.message, {
          hideProgressBar: true,
        });
      }
    },
  });

  return (
    <div>
      <Header />
      <div className="pt-2">
        <div
          className="card mr-auto ml-auto mt-3 mb-2"
          style={{
            width: "35%",
            backgroundColor: "#bdbbbb",
          }}
        >
          <h2 className="card-title">Simple search</h2>
          <form
            onSubmit={formik.handleSubmit}
            style={{ width: "90%", margin: "auto" }}
          >
            <div className="form-group">
              <label htmlFor="field">Choose filed for search:</label>
              <select
                value={formik.values.field}
                name="field"
                className="form-control"
                onChange={formik.handleChange}
              >
                <option value="" disabled hidden>
                  Choose one
                </option>
                <option value="title"> Title </option>
                <option value="writerName"> Writers Name </option>
                <option value="writerLastName">Writers Last Name </option>
                <option value="content"> Content </option>
                <option value="genre"> Genre </option>
              </select>
              {formik.touched.field && formik.errors.field ? (
                <p style={{ color: "red" }}>{formik.errors.field}</p>
              ) : null}
            </div>

            <div className="form-group">
              <label htmlFor="value">Search</label>
              <input
                className="form-control"
                id="value"
                name="value"
                type="text"
                placeholder="Type here"
                onChange={formik.handleChange}
                value={formik.values.value}
              />
              {formik.touched.value && formik.errors.value ? (
                <p style={{ color: "red" }}>{formik.errors.value}</p>
              ) : null}
            </div>

            <div className="form-group">
              <label htmlFor="phrase">Phrase query</label>
              <input
                className="form-control"
                type="checkbox"
                name="phrase"
                id="phrase"
                onChange={formik.handleChange}
                checked={formik.values.phrase}
                value={formik.values.phrase}
              />
            </div>

            <button type="submit" className="btn btn-primary mb-3">
              Search
            </button>
          </form>
        </div>
      </div>
      {searchDone && <h2>Books</h2>}
      {searchDone && (
        <div className="pb-4">
          {" "}
          {books.map((book) => {
            return (
              <div
                className="card mr-auto ml-auto mt-3"
                style={{
                  width: "40%",
                  backgroundColor: "#bdbbbb",
                }}
              >
                <p>
                  {" "}
                  <b>Title:</b> {book.title}
                </p>
                <p>
                  {" "}
                  <b>Genre:</b> {book.genre}
                </p>
                <p>
                  {" "}
                  <b>Writer:</b> {book.writer}
                </p>
                <p>{findHighlights(book.highlights)}</p>
                <p>
                  {" "}
                  {!book.openAccess && (
                    <Button
                      style={{ borderRadius: "2em" }}
                      variant="success"
                      onClick={() => {
                        addToCart(book, 1);
                      }}
                    >
                      Add to cart
                    </Button>
                  )}
                  {book.openAccess && (
                    <span>
                      <a href={downloadUrl + book.pdf}> Download</a>
                    </span>
                  )}
                </p>
                <p>
                  <Button
                    style={{ borderRadius: "2em" }}
                    variant="primary"
                    onClick={() => {
                      seeBookDetails(book.id);
                    }}
                  >
                    Details
                  </Button>
                </p>
              </div>
            );
          })}
        </div>
      )}

      {showDetails && (
        <Modal
          size="lg"
          show={showDetails}
          onHide={handleCancel}
          backdrop="static"
          keyboard={false}
        >
          <Modal.Header
            style={{ backgroundColor: "rgb(52, 58, 64)", color: "white" }}
          >
            <Modal.Title style={{ marginLeft: "auto", marginRight: "auto" }}>
              {bookToShow.title}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <h6 style={{ display: "inline" }}>Genre:</h6>
            <span> {bookToShow.genre}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>ISBN:</h6>
            <span> {bookToShow.isbn}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Number of pages:</h6>
            <span> {bookToShow.numOfPages}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Writer:</h6>
            <span> {bookToShow.writer}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Publisher:</h6>
            <span> {bookToShow.publisher}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Publisher's address:</h6>
            <span> {bookToShow.publishersAddress}</span>
            <br></br>
            <h6 style={{ display: "inline" }}>Year:</h6>
            <span> {bookToShow.year}</span>
            <br></br>
            {bookToShow.openAccess && (
              <>
                <h6 style={{ display: "inline" }}>Open access:</h6>
                <span>
                  <a href={downloadUrl + bookToShow.pdf}> Download</a>
                </span>
                <br></br>
              </>
            )}
            <h6>Synopsis:</h6>
            <span> {bookToShow.synopsis}</span>
            <br></br>
            <br></br>
            <h5 style={{ display: "inline" }}>
              Price: {bookToShow.price}&#36;
            </h5>
            <br></br>
          </Modal.Body>
          <Modal.Footer>
            Amount:
            <Select
              value={selectedAmount}
              onChange={(selectedValue) => {
                console.log(selectedValue);
                setSelectedAmount(selectedValue);
              }}
              options={AMOUNT}
            />
            <Button
              style={{ borderRadius: "2em" }}
              variant="success"
              onClick={() => addToCart(bookToShow, selectedAmount.value)}
            >
              Add to cart
            </Button>
            <Button
              style={{ borderRadius: "2em" }}
              variant="secondary"
              onClick={handleCancel}
            >
              Cancel
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </div>
  );
};

export default SearchBooks;
