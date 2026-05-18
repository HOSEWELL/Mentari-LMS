<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mentari LMS | Empowering Education</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary: #3498db;
            --secondary: #2c3e50;
            --light: #f8f9fa;
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            line-height: 1.6;
            color: #333;
        }

        /* --- FIXED Automated Scrolling Ticker Styles --- */
        .ticker-wrapper {
            width: 100%;
            background-color: var(--secondary);
            color: white;
            padding: 15px 0;
            overflow: hidden;
            position: relative;
            border-bottom: 3px solid var(--primary);
        }

        /* Professional Gradient Fades */
        .ticker-wrapper::before,
        .ticker-wrapper::after {
            content: "";
            height: 100%;
            width: 150px;
            position: absolute;
            top: 0;
            z-index: 2;
            pointer-events: none;
        }

        .ticker-wrapper::before {
            left: 0;
            background: linear-gradient(to right, var(--secondary), rgba(44, 62, 80, 0));
        }

        .ticker-wrapper::after {
            right: 0;
            background: linear-gradient(to left, var(--secondary), rgba(44, 62, 80, 0));
        }

        .ticker {
            display: flex;
            width: fit-content; /* Critical: Fits both lists side-by-side */
            animation: scroll-loop 30s linear infinite; /* Starts immediately */
        }

        .ticker-list {
            display: flex;
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .ticker-list li {
            padding: 0 40px;
            font-size: 1rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 1px;
            white-space: nowrap;
        }

        /* Fixed Loop Animation */
        @keyframes scroll-loop {
            0% {
                transform: translateX(0);
            }
            100% {
                /* Moves left by exactly one full list length */
                transform: translateX(-50%);
            }
        }

        .ticker-wrapper:hover .ticker {
            animation-play-state: paused;
        }

        /* --- Navbar & General Styles --- */
        nav {
            background: white;
            padding: 1rem 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            color: var(--primary);
            text-decoration: none;
        }

        .nav-links a {
            text-decoration: none;
            transition: 0.3s;
        }

        .btn-portal {
            background: var(--primary);
            color: white !important;
            padding: 10px 25px;
            border-radius: 5px;
            font-weight: 500;
        }

        .btn-portal:hover {
            background: #2980b9;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .hero {
            background: linear-gradient(rgba(44, 62, 80, 0.8), rgba(44, 62, 80, 0.8)),
                        url('https://i.pinimg.com/736x/73/18/aa/7318aafa6355a43e259f6ad9adf273d9.jpg');
            background-size: cover;
            background-position: center;
            color: white;
            height: 60vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            padding: 0 20px;
        }

        .hero h1 {
            font-size: 3rem;
            margin-bottom: 10px;
        }

        .section {
            padding: 80px 10%;
            text-align: center;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 40px;
            margin-top: 40px;
        }

        .feature-card {
            padding: 30px;
            background: var(--light);
            border-radius: 10px;
            transition: transform 0.3s ease;
        }

        .feature-card:hover {
            transform: translateY(-5px);
        }

        .feature-card i {
            font-size: 3rem;
            color: var(--primary);
            margin-bottom: 20px;
        }

        footer {
            background: var(--secondary);
            color: white;
            padding: 40px 10%;
            text-align: center;
        }
    </style>
</head>
<body>

    <nav>
        <a href="${pageContext.request.contextPath}/" class="logo">
            <i class="fas fa-graduation-cap"></i> Mentari LMS
        </a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/app/auth/login" class="btn-portal">
                Login Portal
            </a>
        </div>
    </nav>

    <header class="hero">
        <h1>Transform Your Learning Experience</h1>
        <p>A modern platform designed for students and educators alike.</p>
    </header>

    <section class="ticker-wrapper">
        <div class="ticker">
            <ul class="ticker-list">
                <li>Software Accounting</li>
                <li>Fintech QA</li>
                <li>DevOps Engineering</li>
                <li>UI/UX Design</li>
                <li>Cloud Security</li>
                <li>API Development</li>
                <li>Data Science</li>
                <li>Agile Methodology</li>
                <li>Blockchain Dev</li>
                <li>Network Security</li>
                <li>Mobile App Dev</li>
                <li>Backend Systems</li>
                <li>Full Stack</li>
                <li>Compliance (KYC)</li>
                <li>Machine Learning</li>
            </ul>
            <ul class="ticker-list">
                <li>Software Accounting</li>
                <li>Fintech QA</li>
                <li>DevOps Engineering</li>
                <li>UI/UX Design</li>
                <li>Cloud Security</li>
                <li>API Development</li>
                <li>Data Science</li>
                <li>Agile Methodology</li>
                <li>Blockchain Dev</li>
                <li>Network Security</li>
                <li>Mobile App Dev</li>
                <li>Backend Systems</li>
                <li>Full Stack</li>
                <li>Compliance (KYC)</li>
                <li>Machine Learning</li>
            </ul>
        </div>
    </section>

    <section class="section">
        <h2>About Mentari LMS</h2>
        <p style="max-width: 800px; margin: 0 auto; font-size: 1.1rem; color: #666;">
            Mentari is a next-generation Learning Management System built to streamline academic administration
            and enhance student engagement through intuitive design and powerful automation.
        </p>

        <div class="grid">
            <div class="feature-card">
                <i class="fas fa-user-shield"></i>
                <h3>Admin Control</h3>
                <p>Manage courses, track student performance, and handle deferral requests with ease using our specialized Admin Dashboard.</p>
            </div>
            <div class="feature-card">
                <i class="fas fa-user-graduate"></i>
                <h3>Student Success</h3>
                <p>Access your course materials, request deferrals, and track your progress in real-time from any device.</p>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2026 Mentari LMS. Built by Hosewell Karanja.</p>
    </footer>

</body>
</html>