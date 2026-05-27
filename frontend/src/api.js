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

  // 1. Automatically handle JSON headers for payloads
  if (fetchOptions.body && !headers["Content-Type"]) {
    headers["Content-Type"] = "application/json";
  }

  // 2. Attach JWT bearer token if required
  if (token && auth) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...fetchOptions,
    headers,
  });

  // 3. Extract text response first to handle empty/blank bodies safely
  const responseText = await response.text();
  const isJson = response.headers.get("content-type")?.includes("application/json");
  const body = isJson && responseText ? JSON.parse(responseText) : null;

  // 4. Handle expired tokens or unauthorized access automatically
  if (response.status === 401 && auth) {
    clearToken();
    window.location.href = "/login"; // Force redirect to login page
    return null;
  }

  // 5. Handle generic HTTP errors cleanly
  if (!response.ok) {
    throw new Error(body?.message || `Request failed with status ${response.status}`);
  }

  return body;
}