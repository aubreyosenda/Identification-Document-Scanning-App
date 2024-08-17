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
        const response = await fetch('http://localhost:5500/api/v1/company/buildings/list?companyID=' + companyID);
        const buildings = await response.json();

        buildings.forEach((building) => {
            const option = document.createElement('option');
            option.value = building.buildingId;
            option.textContent = building.buildingName;
            buildingSelector.appendChild(option);
        });
    } catch (error) {
        console.error("Error fetching building data:", error);
    }
}

async function displaySecurityPersonnel() {
    const buildingSelector = document.getElementById('buildingSelector');
    const buildingId = buildingSelector.value;

    if (buildingId) {
        try {
            const response = await fetch(`/api/v1/company/building/securityPersonnel?BuildingID=${buildingId}`);
            const personnelList = await response.json();

            const personnelContainer = document.getElementById('securityPersonnelContainer');
            personnelContainer.innerHTML = ''; // Clear existing personnel

            if (Array.isArray(personnelList) && personnelList.length > 0) {
                personnelList.forEach(personnel => {
                    const personnelDiv = document.createElement('div');
                    personnelDiv.className = 'card mb-3';
                    personnelDiv.innerHTML = `
                        <div class="card-body">
                            <h5 class="card-title">${personnel.fullName || 'No Name Provided'}</h5>
                            <p class="card-text"><strong>National ID:</strong> ${personnel.nationalIdNumber || 'N/A'}</p>
                            <p class="card-text"><strong>Work ID:</strong> ${personnel.workIdNumber || 'N/A'}</p>
                            <p class="card-text"><strong>Phone:</strong> ${personnel.phoneNumber || 'N/A'}</p>
                        </div>
                    `;
                    personnelContainer.appendChild(personnelDiv);
                });
            } else {
                console.warn("No security personnel found for the selected building.");
                personnelContainer.innerHTML = '<p class="text-muted">No security personnel assigned to this building.</p>';
            }
        } catch (error) {
            console.error("Error fetching security personnel data:", error);
            const personnelContainer = document.getElementById('securityPersonnelContainer');
            personnelContainer.innerHTML = '<p class="text-danger">An error occurred while fetching security personnel data.</p>';
        }
    } else {
        console.warn("No building selected.");
    }
}
