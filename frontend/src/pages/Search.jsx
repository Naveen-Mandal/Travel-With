import { useCallback, useEffect, useState } from "react";
import { apiFetch } from "../api.js";

const initialForm = {
  sourceStation: "",
  destinationStation: "",
  stationTime: "",
  destArrivalTime: "",
  trainNo: "",
  journeyDate: "",
  journeyEndDate: "",
};

export default function Search() {
  const [form, setForm] = useState(initialForm);
  const [matches, setMatches] = useState(null);
  const [sourceStations, setSourceStations] = useState([]);
  const [destinationStations, setDestinationStations] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const loadStations = useCallback(async (query) => {
    if (query.trim().length < 2) {
      return [];
    }

    return apiFetch(`/api/stations?q=${encodeURIComponent(query)}`, {
      auth: false,
      method: "GET",
    });
  }, []);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      loadStations(form.sourceStation)
        .then(setSourceStations)
        .catch(() => setSourceStations([]));
    }, 250);

    return () => window.clearTimeout(timer);
  }, [form.sourceStation, loadStations]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      loadStations(form.destinationStation)
        .then(setDestinationStations)
        .catch(() => setDestinationStations([]));
    }, 250);

    return () => window.clearTimeout(timer);
  }, [form.destinationStation, loadStations]);

  function updateField(event) {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  }

  function selectStation(name, value) {
    setForm((current) => ({ ...current, [name]: value }));

    if (name === "sourceStation") {
      setSourceStations([]);
    } else if (name === "destinationStation") {
      setDestinationStations([]);
    }
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const data = await apiFetch("/api/travel/search", {
        method: "POST",
        body: JSON.stringify(form),
      });
      setMatches(data);
    } catch (err) {
      setError(err.message);
      setMatches(null);
    } finally {
      setLoading(false);
    }
  }

  return (
    <section>
      <h1 className="text-2xl font-semibold text-slate-950">Find co-travelers</h1>
      <form onSubmit={handleSubmit} className="mt-6 grid gap-4 rounded-lg border border-slate-200 bg-white p-5 shadow-sm md:grid-cols-2">
        <StationInput name="sourceStation" label="Source station" value={form.sourceStation} onChange={updateField} onSelect={selectStation} suggestions={sourceStations} />
        <StationInput name="destinationStation" label="Destination station" value={form.destinationStation} onChange={updateField} onSelect={selectStation} suggestions={destinationStations} />
        <Field name="stationTime" label="Departure time" type="time" value={form.stationTime} onChange={updateField} />
        <Field name="destArrivalTime" label="Destination arrival time" type="time" value={form.destArrivalTime} onChange={updateField} />
        <Field name="trainNo" label="Train number" value={form.trainNo} onChange={updateField} required={false} />
        <Field name="journeyDate" label="Journey date" type="date" value={form.journeyDate} onChange={updateField} />
        <Field name="journeyEndDate" label="Journey end date" type="date" value={form.journeyEndDate} onChange={updateField} required={false} />
        <div className="md:col-span-2">
          {error && <p className="mb-3 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}
          <button
            type="submit"
            disabled={loading}
            className="rounded-md bg-teal-700 px-4 py-2 font-medium text-white hover:bg-teal-600 disabled:cursor-not-allowed disabled:bg-slate-400"
          >
            {loading ? "Searching..." : "Search"}
          </button>
        </div>
      </form>

      {matches && (
        <div className="mt-8 grid gap-6 md:grid-cols-2">
          <MatchList title="Matches at source" items={matches.matchesAtSource} />
          <MatchList title="Matches at destination" items={matches.matchesAtDestination} />
        </div>
      )}
    </section>
  );
}

function Field({ label, required = true, ...props }) {
  return (
    <label className="block">
      <span className="text-sm font-medium text-slate-700">{label}</span>
      <input
        {...props}
        required={required}
        className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2 outline-none focus:border-teal-600 focus:ring-2 focus:ring-teal-100"
      />
    </label>
  );
}

function StationInput({ suggestions, onSelect, ...props }) {
  return (
    <div>
      <Field {...props} />
      {suggestions.length > 0 && (
        <div className="mt-2 rounded-md border border-slate-200 bg-slate-50 p-2">
          {suggestions.map((station) => (
            <button
              key={station.id}
              type="button"
              onClick={() => onSelect(props.name, station.name)}
              className="block w-full rounded px-2 py-1 text-left text-sm text-slate-700 hover:bg-white"
            >
              {station.name}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

function MatchList({ title, items }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <h2 className="font-semibold text-slate-950">{title}</h2>
      {items.length === 0 ? (
        <p className="mt-3 text-sm text-slate-500">No matches yet.</p>
      ) : (
        <div className="mt-4 space-y-3">
          {items.map((match) => (
            <div key={`${match.phoneNo}-${match.journeyDate}`} className="rounded-md border border-slate-200 p-3">
              <p className="font-medium text-slate-950">{match.name}</p>
              <p className="text-sm text-slate-600">{match.phoneNo}</p>
              <p className="mt-2 text-sm text-slate-600">
                {match.sourceStation} to {match.destinationStation}
              </p>
              <p className="text-sm text-slate-600">
                {match.journeyDate} | {match.trainDepartureTime}
              </p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
