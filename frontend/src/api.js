const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export function getToken() {
  return localStorage.getItem("travelWithToken");
}

export function setToken(token) {
  localStorage.setItem("travelWithToken", token);
}

export function clearToken() {
  localStorage.removeItem("travelWithToken");
}

export async function apiFetch(path, options = {}) {
  const { auth = true, ...fetchOptions } = options;
  const token = getToken();
  const headers = {
    ...fetchOptions.headers,
  };

  if (fetchOptions.body && !headers["Content-Type"]) {
    headers["Content-Type"] = "application/json";
  }

  if (token && auth) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...fetchOptions,
    headers,
  });

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json") ? await response.json() : null;

  if (!response.ok) {
    throw new Error(body?.message || "Request failed");
  }

  return body;
}
