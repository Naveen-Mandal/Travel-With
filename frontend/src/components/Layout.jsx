import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { clearToken, getToken } from "../api.js";

export default function Layout() {
  const navigate = useNavigate();
  const isLoggedIn = Boolean(getToken());

  function handleLogout() {
    clearToken();
    navigate("/login");
  }

  return (
    <div className="min-h-screen">
      <nav className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-4 py-3">
          <Link to="/search" className="text-lg font-semibold text-slate-950">
            Travel With
          </Link>
          <div className="flex items-center gap-3">
            {isLoggedIn && (
              <NavLink
                to="/search"
                className={({ isActive }) =>
                  `text-sm font-medium ${isActive ? "text-teal-700" : "text-slate-600"}`
                }
              >
                Search
              </NavLink>
            )}
            {isLoggedIn ? (
              <button
                type="button"
                onClick={handleLogout}
                className="rounded-md bg-slate-900 px-3 py-2 text-sm font-medium text-white hover:bg-slate-700"
              >
                Logout
              </button>
            ) : (
              <NavLink
                to="/login"
                className="rounded-md bg-teal-700 px-3 py-2 text-sm font-medium text-white hover:bg-teal-600"
              >
                Login
              </NavLink>
            )}
          </div>
        </div>
      </nav>
      <main className="mx-auto max-w-5xl px-4 py-8">
        <Outlet />
      </main>
    </div>
  );
}
