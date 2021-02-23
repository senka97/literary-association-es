import React, { useState } from "react";
import Header from "./Header";
import { searchService } from "../services/search-service";
import { bookService } from "../services/book-service";
import { shoppingCartService } from "../services/shopping-cart-service";
import { toast } from "react-toastify";
import { Button, Modal } from "react-bootstrap";
import { AMOUNT } from "../constants";
import Select from "react-select";

const AdvancedSearch = () => {
  const [books, setBooks] = useState([]);
  const [counter, setCounter] = useState(2);
  const [data, setData] = useState([
    { id: 0, fieldName: "", value: "", phrase: false, operation: "" },
    { id: 1, fieldName: "", value: "", phrase: false, operation: "" },
  ]);
  const [searchDone, setSearchDone] = useState(false);
  const [bookToShow, setBookToShow] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const [selectedAmount, setSelectedAmount] = useState({
    value: 1,
    label: "1",
  });
  const downloadUrl = "https://localhost:9000/api/task/downloadFile?filePath=";

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

  const handleChange = (e) => {
    console.log(e.target);
    const { id, value, name } = e.target;
    console.log("ID " + id);
    console.log("Ime polja " + name);
    console.log("Vrednost " + value);

    let newArray = [...data];
    newArray[id][name] = value;
    setData(newArray);
  };

  const handleChangeCheckbox = (e) => {
    const { id, name } = e.target;
    console.log(e.target.checked);

    let newArray = [...data];
    newArray[id][name] = e.target.checked;
    setData(newArray);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    }

    if (data.length < 2) {
      toast.error("You need to make at least two queries", {
        hideProgressBar: true,
      });
      return;
    }

    console.log(data);
    try {
      setSearchDone(false);
      const response = await searchService.advancedSearch(data);
      console.log(response);

      if (response.length === 0) {
        toast.info("No books found. Try again with different arguments.", {
          hideProgressBar: true,
        });
      } else {
        setBooks(response);
        setSearchDone(true);
        let newArray = [
          { id: 0, fieldName: "", value: "", phrase: false, operation: "" },
          { id: 1, fieldName: "", value: "", phrase: false, operation: "" },
        ];

        setData(newArray);
        setCounter(2);
      }
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const createBooleanQuery = () => {
    var formValue = {
      id: counter,
      fieldName: "",
      value: "",
      phrase: false,
      operation: "",
    };
    data.push(formValue);
    setCounter(counter + 1);
  };

  return (
    <div>
      <Header />
      <h1> Advanced Search </h1>

      <Button
        style={{ borderRadius: "2em" }}
        variant="secondary"
        onClick={createBooleanQuery}
      >
        Create New Boolean Query
      </Button>
      <form onSubmit={handleSubmit} style={{ width: "100%", margin: "auto" }}>
        {data.map((element) => {
          return (
            <div
              key={element.id}
              className="card mr-auto ml-auto mt-3 mb-2 px-5"
              style={{
                width: "35%",
                backgroundColor: "#bdbbbb",
              }}
            >
              <div className="form-group">
                <label htmlFor="field">Choose filed for search:</label>
                <select
                  id={element.id}
                  value={element.fieldName}
                  name="fieldName"
                  className="form-control"
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled hidden>
                    Choose one
                  </option>
                  <option value="title"> Title </option>
                  <option value="writerName"> Writers Name </option>
                  <option value="writerLastName"> Writers Last Name </option>
                  <option value="content"> Content </option>
                  <option value="genre"> Genre </option>
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="value">Search</label>
                <input
                  id={element.id}
                  className="form-control"
                  name="value"
                  value={element.value}
                  type="text"
                  placeholder="Type here"
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="field">Choose operation:</label>
                <select
                  id={element.id}
                  value={element.operation}
                  name="operation"
                  className="form-control"
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled hidden>
                    Choose one
                  </option>
                  <option value="AND"> AND </option>
                  <option value="OR"> OR </option>
                  <option value="NOT"> NOT </option>
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="phrase">Phrase query</label>
                <input
                  id={element.id}
                  className="form-control"
                  type="checkbox"
                  value={element.phrase}
                  name="phrase"
                  onChange={handleChangeCheckbox}
                />
              </div>
            </div>
          );
        })}
        <button type="submit" className="btn btn-primary mb-3">
          Search
        </button>
      </form>

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

export default AdvancedSearch;
