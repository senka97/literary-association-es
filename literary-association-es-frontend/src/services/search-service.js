import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class SearchService extends HttpService {
  indexBooks = async () => {
    const response = await this.client.get(ROUTES.INDEX_BOOKS);
    return response.data;
  };

  indexBetaReaders = async () => {
    const response = await this.client.get(ROUTES.INDEX_BETA_READERS);
    return response.data;
  };

  simpleSearch = async (payload) => {
    const response = await this.client.post(ROUTES.SIMPLE_SEARCH, payload);
    return response.data;
  };

  advancedSearch = async (payload) => {
    const response = await this.client.post(ROUTES.ADVANCED_SEARCH, payload);
    return response.data;
  };
}

export const searchService = new SearchService();
