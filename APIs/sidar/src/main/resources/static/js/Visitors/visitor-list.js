function formatPhoneNumber(phoneNumber) {
    const phoneStr = phoneNumber.toString();
    const formatted = `+${phoneStr.slice(0, 3)}-${phoneStr.slice(3, 6)}-${phoneStr.slice(6, 9)}-${phoneStr.slice(8)}`;
    return formatted.slice(0, 9) + '***-' + formatted.slice(14);
}

async function fetchVisitors() {
    const response = await fetch('/api/v1/visitor/show-list');
    const visitors = await response.json();
    const tableBody = document.getElementById('visitorsTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = ''; // Clear the existing rows
    visitors.forEach(visitor => {
        const row = tableBody.insertRow();
        const formattedPhone = formatPhoneNumber(visitor.visitorPhone);
        row.innerHTML = `
            <td>${visitor.id}</td>
            <td>${visitor.visitorFullName}</td>
            <td>${visitor.visitorDocNo}</td>
            <td>${formattedPhone}</td>
            <td>${visitor.organization}</td>
            <td>${visitor.signInTime ? new Date(visitor.signInTime).toLocaleString() : ''}</td>
            <td>${visitor.signOutTime ? new Date(visitor.signOutTime).toLocaleString() : ''}</td>
            <td>
                ${visitor.signOutTime
            ? '<img src="../../../static/images/check_24dp.svg" alt="Signed Out">'
            : `<img src="../../../static/images/logout_29dp.svg" alt="Sign Out" style="cursor: pointer;" onclick="signOutVisitor('${visitor.visitorDocNo}')">`}
            </td>
        `;
    });
}

setInterval(fetchVisitors, 60000); // Refresh the list every 60 seconds

async function signOutVisitor(documentNo) {
    const response = await fetch(`/api/v1/visitor/signout`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({documentNo, signedOutBy: "Jamesx"})
    });
    const result = await response.text();
    alert(result);
    location.reload();
    fetchVisitors(); // Refresh the list after signing out
}

fetchVisitors();
