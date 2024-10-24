import React, { useState } from 'react';
import './App.css';

function App() {
  const [rule, setRule] = useState(''); // Single rule input
  const [userData, setUserData] = useState({
    age: 0,
    department: '',
    salary: 0,
    experience: 0
  });
  const [result, setResult] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    const parsedValue = name === 'age' || name === 'salary' || name === 'experience' 
      ? parseInt(value) 
      : value;

    setUserData((prevData) => ({ ...prevData, [name]: parsedValue }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); // Clear any previous errors
    setResult(''); // Clear previous results

    // Validate numeric inputs
    const { age, salary, experience } = userData;
    if (age < 0 || salary < 0 || experience < 0) {
      setError('Age, salary, and experience must be non-negative.');
      return;
    }

    try {
      // Call backend API to evaluate the rule
      const response = await fetch('http://localhost:9090/rule-engine/evaluate_rule', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ruleString: rule,
          data: userData,
        })
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
      }

      const resultData = await response.json();
      setResult(resultData ? "Eligible" : "Not Eligible");
    } catch (err) {
      setError('Failed to evaluate rule. ' + err.message);
    }
  };

  return (
    <div className="App">
      <h2>Rule Engine</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Rule: </label>
          <input
            type="text"
            value={rule}
            onChange={(e) => setRule(e.target.value)}
            placeholder="Enter your rule"
            required
          />
        </div>
        <h3>User Data</h3>
        <div className="form-group">
          <label>Age: </label>
          <input
            type="number"
            name="age"
            value={userData.age}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Department: </label>
          <input
            type="text"
            name="department"
            value={userData.department}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Salary: </label>
          <input
            type="number"
            name="salary"
            value={userData.salary}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Experience: </label>
          <input
            type="number"
            name="experience"
            value={userData.experience}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Evaluate</button>
      </form>
      {result && <h4 className="result">Result: {result}</h4>}
      {error && <h4 className="error">Error: {error}</h4>}
    </div>
  );
}

export default App;
