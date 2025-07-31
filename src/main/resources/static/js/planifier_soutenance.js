// /static/js/planifier_soutenance.js
document.getElementById('findJuryBtn').addEventListener('click', function() {
    const date = document.getElementById('dateSoutenance').value;
    const heure = document.getElementById('heureSoutenance').value;

    if (!date || !heure) {
        alert('Veuillez d\'abord sélectionner une date et une heure.');
        return;
    }

    const debutDateTime = date + 'T' + heure + ':00'; // Ajout des secondes pour être conforme

    fetch(`/api/admin/enseignants-disponibles?debut=${debutDateTime}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erreur réseau ou serveur lors de la récupération du jury.');
            }
            return response.json();
        })
        .then(enseignants => {
            const jurySection = document.getElementById('jury-section');
            const juryListDiv = document.getElementById('jury-list');
            
            juryListDiv.innerHTML = ''; 
            
            if (enseignants.length === 0) {
                juryListDiv.innerHTML = '<p style="color: #ef4444;">Aucun enseignant n\'est disponible pour ce créneau.</p>';
            } else {
                enseignants.forEach(enseignant => {
                    const label = document.createElement('label');
                    label.innerHTML = `<input type="checkbox" name="juryEnseignants" value="${enseignant.id}"> ${enseignant.prenom} ${enseignant.nom}`;
                    juryListDiv.appendChild(label);
                });
            }
            
            jurySection.style.display = 'block';
        })
        .catch(error => {
            console.error('Erreur:', error);
            alert(error.message);
        });
});