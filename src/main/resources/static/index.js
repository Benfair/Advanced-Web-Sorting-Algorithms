let selectedRow = null;

function fetchParticipants() {
    fetch('/api/participants')
        .then(response => response.json())
        .then(data => updateLeaderboard(data))
        .catch(error => console.error('Error:', error));
}

function searchParticipants() {
    const searchTerm = document.getElementById('searchInput').value;
    fetch(`/api/participants/search?name=${searchTerm}`)
        .then(response => response.json())
        .then(data => updateLeaderboard(data))
        .catch(error => console.error('Error:', error));
}

function getSortedParticipants() {
    const algorithm = document.getElementById('sortAlgorithm').value;
    fetch(`/api/participants/sorted?algorithm=${algorithm}`)
        .then(response => response.json())
        .then(data => updateLeaderboard(data))
        .catch(error => console.error('Error:', error));
}

function updateLeaderboard(participants) {
    const leaderboardBody = document.getElementById('leaderboardBody');
    leaderboardBody.innerHTML = '';
    participants.forEach((participant, index) => {
        const row = document.createElement('tr');
        row.dataset.id = participant.id; // Add data-id attribute to the row
        row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${participant.name}</td>
                    <td>${participant.score}</td>
                    <td></td>
                `;
        const cell = row.lastElementChild;
        const selectButton = document.createElement('button');
        selectButton.textContent = 'Select';
        selectButton.addEventListener('click', () => selectParticipant(participant.id, participant.name, participant.score));
        cell.appendChild(selectButton);
        leaderboardBody.appendChild(row);
    });
}

function addParticipant() {
    const name = document.getElementById('addName').value;
    const score = document.getElementById('addScore').value;
    fetch('/api/participants', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, score }),
    })
        .then(response => response.json())
        .then(() => {
            fetchParticipants();
            document.getElementById('addName').value = '';
            document.getElementById('addScore').value = '';
        })
        .catch(error => console.error('Error:', error));
}

function selectParticipant(id, name, score) {
    document.getElementById('editId').value = id;
    document.getElementById('editScore').value = score;
    document.getElementById('editForm').style.display = 'block';

    if (selectedRow) {
        selectedRow.classList.remove('selected');
    }
    selectedRow = document.querySelector(`tr[data-id="${id}"]`);
    if (selectedRow) {
        selectedRow.classList.add('selected');
    }
}

function updateScore() {
    const id = document.getElementById('editId').value;
    const score = document.getElementById('editScore').value;
    fetch(`/api/participants/${id}/score?score=${score}`, {
        method: 'PATCH',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update score.');
            }
            return response.json();
        })
        .then(() => {
            fetchParticipants();
            document.getElementById('editForm').style.display = 'none';
        })
        .catch(error => console.error('Error:', error));
}

function deleteParticipant() {
    const id = document.getElementById('editId').value;
    fetch(`/api/participants/${id}`, {
        method: 'DELETE',
    })
        .then(() => {
            fetchParticipants();
            document.getElementById('editForm').style.display = 'none';
        })
        .catch(error => console.error('Error:', error));
}

// Initial fetch
fetchParticipants();