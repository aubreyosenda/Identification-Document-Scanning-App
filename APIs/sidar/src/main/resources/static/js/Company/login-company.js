// Show/Hide password functionality
document.getElementById('show-passwords').addEventListener('change', function() {
    const password = document.getElementById('password');
    if (this.checked) {
        password.type = 'text';
    } else {
        password.type = 'password';
    }
});

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = {
        companyEmailAddress: document.getElementById('email').value.trim(),
        companyPassword: document.getElementById('password').value.trim()
    };

    fetch('/api/v1/company/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.message === "Login successful") {
                localStorage.setItem('companyProfile', JSON.stringify(data.profile));
                window.location.href = '/company/profile';
            } else {
                alert('Login failed: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while logging in');
        });
});