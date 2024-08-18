document.addEventListener("DOMContentLoaded", () => {
    const companyProfile = JSON.parse(localStorage.getItem('companyProfile'));

    if (companyProfile) {
        const companyID = companyProfile.companyId;
        loadBuildingOptions(companyID);
    }
});

async function loadBuildingOptions(companyID) {
    const buildingSelector = document.getElementById('buildingSelector');

    try {
        const response = await fetch('/api/v1/company/buildings/list?companyID=' + companyID);
        const buildings = await response.json();

        buildings.forEach((building) => {
            const option = document.createElement('option');
            option.value = building.buildingId;
            option.textContent = building.buildingName;
            buildingSelector.appendChild(option);
        });

        // Add an event listener to load floors when a building is selected
        buildingSelector.addEventListener('change', () => {
            loadFloors(); // Load floors whenever a building is selected
        });
    } catch (error) {
        console.error("Error fetching building data:", error);
    }
}

function loadFloors() {
    const floorsContainer = document.getElementById('floorsContainer');
    floorsContainer.innerHTML = ''; // Clear existing floors

    const maxFloors = 10; // Define a reasonable number of floors for demo purposes
    for (let floorNumber = 0; floorNumber < maxFloors; floorNumber++) {
        createFloorDiv(floorNumber);
    }
}

function createFloorDiv(floorNumber) {
    const floorsContainer = document.getElementById('floorsContainer');
    const floorDiv = document.createElement('div');
    floorDiv.className = 'floor mb-4';
    floorDiv.innerHTML = `
        <h5 onclick="displayOrganizations(${floorNumber})" style="cursor: pointer;">${getFloorLabel(floorNumber)}</h5>
        <div id="organizationsContainer-${floorNumber}" class="organizations-container" style="display: none;"></div>
    `;
    floorsContainer.appendChild(floorDiv);
}

async function displayOrganizations(floorNumber) {
    const buildingSelector = document.getElementById('buildingSelector');
    const buildingId = buildingSelector.value;
    const organizationsContainer = document.getElementById(`organizationsContainer-${floorNumber}`);

    if (buildingId && organizationsContainer.style.display === 'none') {
        try {
            const response = await fetch(`/api/v1/company/building/organizations/byFloor?BuildingID=${buildingId}&floorNumber=${floorNumber}`);
            const organizations = await response.json();

            organizationsContainer.innerHTML = organizations.length > 0
                ? organizations.map(org => `<div class="organization">${org.org_name || org.organizationName || 'Unnamed Organization'}</div>`).join('')
                : '<div class="organization">No organizations on this floor</div>';

            toggleOrganizations(floorNumber);
        } catch (error) {
            console.error(`Error fetching organization data for floor ${floorNumber}:`, error);
        }
    } else {
        toggleOrganizations(floorNumber);
    }
}

function toggleOrganizations(floorIndex) {
    const organizationsContainer = document.getElementById(`organizationsContainer-${floorIndex}`);
    const isHidden = organizationsContainer.style.display === 'none';
    document.querySelectorAll('.organizations-container').forEach(container => {
        container.style.display = 'none';
    });
    organizationsContainer.style.display = isHidden ? 'block' : 'none';
}

function getFloorLabel(floorNumber) {
    if (floorNumber === 0) return "Ground Floor";
    return `${floorNumber} Floor`;
}
