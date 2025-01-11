import React, { useState } from "react";

const Login: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLogin = () => {
    setLoading(true);
    setError("");
    const audience = "http://api.noodlestar.com";
  
    // Redirect the user to the Auth0 Universal Login page
    window.location.href = `https://dev-5kbvxb8zgblo1by3.us.auth0.com/authorize?` +
      //`response_type=token&` +
      //`client_id=Pr9b4hkdMFcmkjKK2dpbLimUJplCuwSM&` +  // Your Auth0 Client ID
      //`redirect_uri=http://localhost:3000/callback&` +  // The redirect URL after login
      `scope=openid profile email&` +                  // Scope to get user information
     // `audience=${audience}&` +                         // Specify the audience for access token
      `prompt=login`;                                  // Force the login page to prompt user credentials
  };

  return (
    <div>
      <h2>Login</h2>
      <button onClick={handleLogin} disabled={loading}>
        {loading ? "Redirecting to Auth0..." : "Login with Auth0"}
      </button>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default Login;
