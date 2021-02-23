import React, { useState } from "react";
import { searchService } from "../services/search-service";
import Header from "./Header";
import Button from "react-bootstrap/Button";
import Select from "react-dropdown-select";
import { toast } from "react-toastify";

const BetaReadersGeoSearch = () => {
  const [genre, setGenre] = useState([]);
  const [genreChosen, setGenreChosen] = useState(false);
  const [betaReaders, setBetaReaders] = useState([]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    let userId = localStorage.getItem("currentUserId");

    try {
      const response = await searchService.geoLocationSearch(userId, genre);
      console.log(response);
      setBetaReaders(response);
      setGenreChosen(true);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  const handleChange = async (event) => {
    setGenre(event.target.value);
  };

  const reset = () => {
    setGenre(null);
    setGenreChosen(false);
    setBetaReaders([]);
  };

  return (
    <div>
      <Header />
      {!genreChosen && (
        <div>
          <h2> Find beta readers for chosen genre </h2>
          <form
            onSubmit={handleSubmit}
            style={{ width: "100%", margin: "auto" }}
          >
            <div
              className="card mr-auto ml-auto mt-3 mb-2 px-5"
              style={{
                width: "35%",
                backgroundColor: "#bdbbbb",
              }}
            >
              <div className="form-group">
                <label htmlFor="field">Choose genre:</label>
                <select
                  id={1}
                  value={genre}
                  name="genreName"
                  className="form-control"
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled hidden>
                    Choose one
                  </option>
                  <option value="Thriller"> Thriller </option>
                  <option value="Romance"> Romance </option>
                  <option value="Drama"> Drama </option>
                  <option value="Crime"> Crime </option>
                  <option value="Biography"> Biography </option>
                  <option value="Classic"> Classic </option>
                  <option value="Psychology"> Biography </option>
                </select>
              </div>
              <Button
                variant="secondary"
                type="submit"
                style={{ marginBottom: "1em", marginTop: "1em" }}
              >
                Submit
              </Button>
            </div>
          </form>
        </div>
      )}
      {genreChosen && (
        <div
          className="card mr-auto ml-auto mt-3 mb-2 px-5"
          style={{
            width: "35%",
            backgroundColor: "#bdbbbb",
          }}
        >
          <h2> Beta reader more than 100km located </h2>
          <Select
            multi={true}
            options={betaReaders.map((br, index) => {
              return {
                label: br.firstName + " " + br.lastName,
                value: index,
                key: index,
              };
            })}
            style={{ backgroundColor: "white", marginBottom: "2em" }}
          />
          <Button
            style={{ borderRadius: "2em", marginBottom: "2em" }}
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

export default BetaReadersGeoSearch;
