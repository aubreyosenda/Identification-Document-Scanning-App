// Function to populate the company profile
function loadCompanyProfile() {
    const companyProfile = JSON.parse(localStorage.getItem('companyProfile'));
    if (companyProfile) {
        document.getElementById('navbar-company-name').textContent = companyProfile.companyName;

        // Populate company stats if needed
        const statsContainer = document.getElementById('company-stats');
        if (statsContainer) {
            for (const [key, value] of Object.entries(companyProfile.stats)) {
                const statElement = document.createElement('p');
                statElement.textContent = `${key}: ${value}`;
                statsContainer.appendChild(statElement);
            }
        }
    }
}

document.addEventListener('DOMContentLoaded', loadCompanyProfile);


// Logout function
document.getElementById('logout-button').addEventListener('click', function() {
    localStorage.removeItem('companyProfile');
    window.location.href = '/login'; // Redirect to login page or homepage
});

// Dummy data for the charts
const floorData = {
    labels: ['Floor 1', 'Floor 2', 'Floor 3'],
    datasets: [{
        label: 'Visitors Over 1 Week',
        data: [20, 50, 30],
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
    }]
};

const orgData = {
    labels: ['Org 1', 'Org 2', 'Org 3', 'Org 4', 'Org 5', 'Org 6', 'Org 7', 'Org 8'],
    datasets: [{
        label: 'Number of Visitors',
        data: [5, 3, 2, 4, 3, 1, 5, 6],
        backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)'
        ],
        borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)'
        ],
        borderWidth: 1
    }]
};

// Initialize Bar Chart
const ctxBar = document.getElementById('barChart').getContext('2d');
new Chart(ctxBar, {
    type: 'bar',
    data: floorData,
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Initialize Pie Chart
const ctxPie = document.getElementById('pieChart').getContext('2d');
new Chart(ctxPie, {
    type: 'pie',
    data: orgData,
    options: {
        responsive: true
    }
});
