import { DataProvider } from "@refinedev/core";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export const adminDataProvider: DataProvider = {
  getList: async ({ resource, pagination }) => {
    const current = pagination?.currentPage ?? 1;
    const pageSize = pagination?.pageSize ?? 20;
    const page = current - 1; // Refine is 1-based, backend is 0-based

    const response = await fetch(
      `${API_URL}/api/v1/admin/${resource}?page=${page}&size=${pageSize}`
    );

    if (!response.ok) throw new Error(`Failed to fetch ${resource}`);

    const json = await response.json();
    return { data: json.data, total: json.total };
  },

  getOne: async ({ resource, id }) => {
    const response = await fetch(`${API_URL}/api/v1/admin/${resource}/${id}`);

    if (response.status === 404) throw new Error("not_found");
    if (!response.ok) throw new Error(`Failed to fetch ${resource}/${id}`);

    const data = await response.json();
    return { data };
  },

  create: async () => { throw new Error("not implemented"); },
  update: async () => { throw new Error("not implemented"); },
  deleteOne: async () => { throw new Error("not implemented"); },
  getApiUrl: () => `${API_URL}/api/v1/admin`,
};
