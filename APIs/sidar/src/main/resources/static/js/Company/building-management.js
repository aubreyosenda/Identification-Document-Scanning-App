document.addEventListener('DOMContentLoaded', function () {
    const showFormButton = document.getElementById('show-form-button');
    const registerForm = document.getElementById('register-building-form');
    const buildingList = document.getElementById('building-items');
    const buildingsTable = document.getElementById('buildingsTable');
    const companyProfile = JSON.parse(localStorage.getItem('companyProfile'));

    registerForm.style.visibility = "hidden";

    if (companyProfile) {
        document.getElementById('navbar-company-name').textContent = companyProfile.companyName;

        // Function to fetch and display buildings
        function loadBuildings() {
            const companyID = companyProfile.companyId;

            fetch(`/api/v1/company/buildings/list?companyID=${companyID}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch buildings');
                    }
                    return response.json();
                })
                .then(data => {
                    // Clear the list before loading
                    buildingList.innerHTML = '';

                    if (data.length > 0) {
                        buildingsTable.style.display = 'table'; // Show the table header

                        data.forEach(building => {
                            const li = document.createElement('tr');
                            li.innerHTML = `
                                <td>${building.buildingName}</td>
                                <td>${building.numberOfFloors}</td>
                                <td><button class="btn btn-danger btn-sm delete-building" data-id="${building.buildingId}">Delete</button></td>
                            `;
                            buildingList.appendChild(li);
                        });
                    } else {
                        buildingsTable.style.display = 'none'; // Hide the table header if no buildings
                    }
                })
                .catch(error => {
                    console.error('Error fetching buildings:', error);
                });
        }

        // Load buildings on page load
        loadBuildings();

        showFormButton.addEventListener('click', function () {
            showFormButton.style.display = 'none';
            registerForm.style.visibility = "visible";
        });

        // Handle building registration
        registerForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const buildingName = document.getElementById('buildingName').value;
            const numberOfFloors = parseInt(document.getElementById('numberOfFloors').value, 10);
            const companyID = companyProfile.companyId;

            // AJAX call to register the building
            fetch('/api/v1/company/buildings/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ buildingName, numberOfFloors, companyID })
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.message) {
                        // Handle success, add the building to the list
                        const li = document.createElement('tr');
                        li.innerHTML = `
                            <td>${buildingName}</td>
                            <td>${numberOfFloors}</td>
                            <td><button class="btn btn-danger btn-sm delete-building" data-id="${data.buildingId}">Delete</button></td>
                        `;
                        buildingList.appendChild(li);
                        buildingsTable.style.display = 'table'; // Show the table header if it was hidden
                    }
                })
                .catch(error => {
                    console.error('Error during registration:', error);
                });

            // registerForm.reset();
            showFormButton.style.display = "block";
            registerForm.style.visibility = "hidden";
            registerForm.reset();
        });

        // Handle building deletion
        buildingList.addEventListener('click', function (e) {
            if (e.target.classList.contains('delete-building')) {
                const buildingId = e.target.getAttribute('data-id');

                fetch(`/api/v1/buildings/${buildingId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.message) {
                            // Remove the building from the list
                            e.target.parentElement.parentElement.remove();
                            if (buildingList.children.length === 0) {
                                buildingsTable.style.display = 'none'; // Hide the table header if no buildings left
                            }
                        }
                    })
                    .catch(error => {
                        console.error('Error deleting building:', error);
                    });
            }
        });
    } else {
        location.reload();
    }
});
