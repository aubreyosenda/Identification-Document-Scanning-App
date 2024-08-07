document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('companyForm');
    const phoneNumberInput = document.getElementById('phone-number');
    const submitBtn = document.getElementById('submit-btn');

    // Show/Hide password functionality
    document.getElementById('show-passwords').addEventListener('change', function() {
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirm-password');
        if (this.checked) {
            password.type = 'text';
            confirmPassword.type = 'text';
        } else {
            password.type = 'password';
            confirmPassword.type = 'password';
        }
    });

    phoneNumberInput.addEventListener('input', ev => {
        // Remove non-numeric characters
        const input = ev.target.value.replace(/\D/g, '');
        const countryCode = document.getElementById('country-code').value;

        // set max length for Kenyan numbers
        let maxLength = 10;
        if (countryCode === "+254") {
            maxLength = 9;
        }

        if (input.length > maxLength) {
            alert(`Phone number cannot exceed ${maxLength} digits for the selected country code`);
            ev.target.value = input.slice(0, maxLength);
        }

        // Update the input value
        ev.target.value = input.replace(/(\d{3})(?=\d)/g, '$1-');
    });

    submitBtn.addEventListener('click', () => {
        // Validate the form
        if (validateForm()) {
            // Collect form data
            const companyData = {
                companyName: document.getElementById('company-name').value.trim(),
                companyEmailAddress: document.getElementById('company-email').value.trim(),
                companyPhoneNumber: validatePhoneNumber(document.getElementById('country-code').value, document.getElementById('phone-number').value.trim()),
                companyPhysicalAddress: document.getElementById('hq-location').value.trim(),
                companyPassword: document.getElementById('password').value.trim()
            };

            // Send data to the API
            fetch('http://localhost:5500/api/v1/company/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(companyData)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw new Error(Object.values(errorData).join(' '));
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    alert('Company registered successfully');
                    form.reset();
                    window.location.href = '/company-login';
                })
                .catch(error => {
                    console.error('Error:', error.message);
                    alert('An error occurred: ' + error.message);
                });
        }
    });

    function validateForm() {
        // Get form elements
        const companyName = document.getElementById('company-name').value.trim();
        const companyEmail = document.getElementById('company-email').value.trim();
        const countryCode = document.getElementById('country-code').value;
        const phoneNumber = document.getElementById('phone-number').value.trim();
        const hqLocation = document.getElementById('hq-location').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirmPassword = document.getElementById('confirm-password').value.trim();

        // Validate each field
        if (companyName === '') {
            alert('Company Name is required');
            return false;
        }
        if (!validateEmail(companyEmail)) {
            alert('Invalid Email Address');
            return false;
        }
        if (!validatePhoneNumber(countryCode, phoneNumber)) {
            alert('Invalid Phone Number');
            return false;
        }
        if (hqLocation === '') {
            alert('Company HQ Location is required');
            return false;
        }
        if (password === '') {
            alert('Password is required');
            return false;
        }
        if (password !== confirmPassword) {
            alert('Passwords do not match');
            return false;
        }

        // If all validations pass, return true
        return true;
    }

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function validatePhoneNumber(countryCode, phone) {
        const phoneNo = countryCode + phone;
        const phoneNumber = phoneNo.replace(/\D/g, '');
        const re = /^\d{10,15}$/;  // 13 digit phone number
        const isValid = re.test(phoneNumber);
        return phoneNumber;
    }
});
