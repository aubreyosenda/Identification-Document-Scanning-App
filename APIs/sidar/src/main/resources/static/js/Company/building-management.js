document.addEventListener('DOMContentLoaded', function () {
    const registerForm = document.getElementById('register-building-form');
    const buildingList = document.getElementById('building-items');

    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const buildingName = document.getElementById('buildingName').value;
        const numberOfFloors = document.getElementById('numberOfFloors').value;

        // AJAX call to register the building
        fetch('/api/v1/buildings/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('authToken') // Assuming you're storing auth token in localStorage
            },
            body: JSON.stringify({ buildingName, numberOfFloors })
        })
            .then(response => response.json())
            .then(data => {
                if (data.message) {
                    // Handle success, add the building to the list
                    const li = document.createElement('li');
                    li.className = 'list-group-item d-flex justify-content-between align-items-center';
                    li.innerHTML = `${buildingName} <button class="btn btn-danger btn-sm delete-building" data-id="${data.buildingId}">Delete</button>`;
                    buildingList.appendChild(li);
                }
            });
    });

    // Fetch and display buildings
    function loadBuildings() {
        fetch('/api/v1/buildings', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authToken')
            }
        })
            .then(response => response.json())
            .then(data => {
                buildingList.innerHTML = '';
                data.forEach(building => {
                    const li = document.createElement('li');
                    li.className = 'list-group-item d-flex justify-content-between align-items-center';
                    li.innerHTML = `${building.buildingName} <button class="btn btn-danger btn-sm delete-building" data-id="${building.buildingId}">Delete</button>`;
                    buildingList.appendChild(li);
                });
            });
    }

    loadBuildings();

    // Handle building deletion
    buildingList.addEventListener('click', function (e) {
        if (e.target.classList.contains('delete-building')) {
            const buildingId = e.target.getAttribute('data-id');

            fetch(`/api/v1/buildings/${buildingId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authToken')
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.message) {
                        // Remove the building from the list
                        e.target.parentElement.remove();
                    }
                });
        }
    });
});
