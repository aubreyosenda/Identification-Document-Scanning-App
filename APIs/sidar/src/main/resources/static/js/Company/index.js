

var navLinks = document.getElementById("navLinks");
  function showMenu() {
    navLinks.style.right = "0";
    }
    function hideMenu() {
        navLinks.style.right = "-200px";

    }




    //BUILDINGS CODE 

    let currentBuildingIndex = 0;

function addCompany(buildingIndex, floorIndex) {
    hidePreviousInputs(`bd-company-${buildingIndex}-${floorIndex}`);

    const companyContainer = document.getElementById(`bd-companiesContainer-${buildingIndex}-${floorIndex}`);
    const companyCount = companyContainer.children.length + 1;

    const companyForm = `
        <div class="mb-2">
            <label for="bd-company-${buildingIndex}-${floorIndex}-${companyCount}" class="form-label">Company ${companyCount}:</label>
            <input type="text" class="form-control" id="bd-company-${buildingIndex}-${floorIndex}-${companyCount}" name="company-${buildingIndex}-${floorIndex}[]" required>
        </div>
    `;
    companyContainer.insertAdjacentHTML('beforeend', companyForm);
}

function addFloor(buildingIndex) {
    hidePreviousInputs(`bd-floor-${buildingIndex}`);

    const floorContainer = document.getElementById(`bd-floorsContainer-${buildingIndex}`);
    const floorCount = floorContainer.children.length;

    const floorLabel = getFloorLabel(floorCount);

    const floorForm = `
        <div id="bd-floor-${buildingIndex}-${floorCount}" class="mb-3 border p-3 rounded">
            <h5>${floorLabel}</h5>
            <div id="bd-companiesContainer-${buildingIndex}-${floorCount}">
                <!-- Companies will be added here -->
            </div>
            <button type="button" class="btn btn-secondary mt-2" onclick="addCompany(${buildingIndex}, ${floorCount})">Add Company</button>
        </div>
    `;
    floorContainer.insertAdjacentHTML('beforeend', floorForm);
}

function getFloorLabel(floorCount) {
    const ordinalSuffixes = ["th", "st", "nd", "rd"];
    const floorMap = {
        0: "Ground Floor"
    };

    if (floorMap[floorCount] !== undefined) {
        return floorMap[floorCount];
    }

    const lastDigit = floorCount % 10;
    const suffix = ordinalSuffixes[(lastDigit <= 3 && Math.floor(floorCount / 10) !== 1) ? lastDigit : 0];

    return `${floorCount}${suffix} Floor`;
}

function addBuilding() {
    hidePreviousInputs(`bd-building-${currentBuildingIndex}`);

    currentBuildingIndex++;
    const container = document.getElementById('buildingsContainer');
    const buildingForm = `
        <div id="bd-building-${currentBuildingIndex}" class="mb-4 border p-3 rounded">
            <h4 id="bd-buildingLabel-${currentBuildingIndex}" style="cursor: pointer;" onclick="toggleBuildingDetails(${currentBuildingIndex})">Building ${currentBuildingIndex} Details</h4>
            <div class="bd-building-details">
                <div class="mb-3">
                    <label for="bd-building-name-${currentBuildingIndex}" class="form-label">Building Name:</label>
                    <input type="text" class="form-control bd-building-name" id="bd-building-name-${currentBuildingIndex}" name="building-name[]" oninput="updateBuildingLabel(${currentBuildingIndex})" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Floors:</label>
                    <div id="bd-floorsContainer-${currentBuildingIndex}">
                        <!-- Floors will be added here -->
                    </div>
                    <button type="button" class="btn btn-secondary mt-2" onclick="addFloor(${currentBuildingIndex})">Add Floor</button>
                </div>
                <button type="button" class="btn btn-success mt-3" onclick="doneBuilding(${currentBuildingIndex})">Done</button>
            </div>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', buildingForm);
}

function updateBuildingLabel(buildingIndex) {
    const buildingName = document.getElementById(`bd-building-name-${buildingIndex}`).value;
    const buildingLabel = document.getElementById(`bd-buildingLabel-${buildingIndex}`);
    buildingLabel.textContent = buildingName ? `${buildingName} Details` : `Building ${buildingIndex} Details`;
}

function hidePreviousInputs(selectorPrefix) {
    const elements = document.querySelectorAll(`[id^="${selectorPrefix}"] input`);
    elements.forEach(el => el.disabled = true);
}

function storeBuildingNames() {
    const buildingNames = Array.from(document.querySelectorAll('.bd-building-name')).map(input => input.value);
    localStorage.setItem('buildingNames', JSON.stringify(buildingNames));
}

function doneBuilding(buildingIndex) {
    const buildingDiv = document.getElementById(`bd-building-${buildingIndex}`);
    buildingDiv.querySelector('.bd-building-details').style.display = 'none';
}

function toggleBuildingDetails(buildingIndex) {
    const buildingDiv = document.getElementById(`bd-building-${buildingIndex}`);
    const detailsDiv = buildingDiv.querySelector('.bd-building-details');
    if (detailsDiv.style.display === 'none' || detailsDiv.style.display === '') {
        detailsDiv.style.display = 'block';
    } else {
        detailsDiv.style.display = 'none';
    }
}


///testimonials

document.addEventListener('DOMContentLoaded', function() {
    const mostReviewedTestimonials = [
        {
            img: 'images/user1.jpg',
            text: "Vanatel's solutions have transformed our approach to data security. Their tools are top-notch and their support is exceptional.",
            name: 'Michael Kiprotich',
            rating: 5
        },
        {
            img: 'images/user2.jpg',
            text: "A great experience with Vanatel. Their data management solutions are efficient and their team is highly knowledgeable.",
            name: 'Jane Njeri',
            rating: 4.5
        }
    ];

    const container = document.getElementById('testimonials-container');
    mostReviewedTestimonials.forEach(testimonial => {
        const col = document.createElement('div');
        col.className = 'testimonials-col';

        col.innerHTML = `
            <img src="${testimonial.img}" alt="${testimonial.name}">
            <div>
                <p>"${testimonial.text}"</p>
                <h3>${testimonial.name}</h3>
                <div class="rating">${generateStars(testimonial.rating)}</div>
            </div>
        `;

        container.appendChild(col);
    });

    function generateStars(rating) {
        let stars = '';
        for (let i = 0; i < Math.floor(rating); i++) {
            stars += '<i class="fas fa-star"></i>';
        }
        if (rating % 1 !== 0) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        }
        while (stars.length < 5) {
            stars += '<i class="far fa-star"></i>';
        }
        return stars;
    }
});
