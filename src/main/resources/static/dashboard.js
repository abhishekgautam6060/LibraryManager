fetch("http://localhost:8080/api/dashboard")
  .then(res => res.json())
  .then(data => {
    document.getElementById("totalSeats").innerText = data.totalSeats;
    document.getElementById("filledSeats").innerText = data.filledSeats;
    document.getElementById("vacantSeats").innerText = data.vacantSeats;
    document.getElementById("totalCollection").innerText = "₹" + data.totalCollection;
  });


fetch("http://localhost:8080/api/seats")
  .then(res => res.json())
  .then(seats => renderSeats(seats));

function renderSeats(seats) {
    const container = document.getElementById("seatContainer");
    container.innerHTML = "";

    const seatsPerRow = 6;
    let row = [];

    seats.forEach((seat, index) => {
        row.push(seat);

        if (row.length === seatsPerRow || index === seats.length - 1) {
            // Zig-zag: reverse alternate rows
            const isReverse = Math.floor(index / seatsPerRow) % 2 !== 0;
            const finalRow = isReverse ? [...row].reverse() : row;

            finalRow.forEach(s => {
                const div = document.createElement("div");
                div.classList.add("seat");
                div.innerText = s.seatNumber;
               if (s.occupied) {
                   div.classList.add("occupied");
                   div.onclick = () => openStudentModal(s.seatNumber);
               } else {
                   div.classList.add("vacant");
                   div.onclick = () => bookSeat(s.seatNumber);
               }

                container.appendChild(div);
            });

            row = [];
        }
    });
}


let selectedSeat = null;

function bookSeat(seatNumber) {
    selectedSeat = seatNumber;
    document.getElementById("seatNo").innerText = seatNumber;
    document.getElementById("bookingModal").style.display = "block";
}



//Confirmbooking flow start here
function confirmBooking() {

    const payload = {
        seatNumber: selectedSeat,
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        amountPaid: document.getElementById("amount").value
    };

    fetch("http://localhost:8080/api/book", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Booking failed");
        return res.text();
    })
    .then(() => {
        closeModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}


function closeModal() {
    document.getElementById("bookingModal").style.display = "none";
}

function refreshUI() {
    location.reload();
}




//Vavocate flow start here

let vacateSeatNumber = null;


function openVacateModal(seatNumber) {
    vacateSeatNumber = seatNumber;
    document.getElementById("vacateSeatNo").innerText = seatNumber;
    document.getElementById("studentInfo").innerText =
        "Are you sure you want to vacate this seat?";
    document.getElementById("vacateModal").style.display = "block";
}


function confirmVacate() {

    fetch(`http://localhost:8080/api/vacate/${vacateSeatNumber}`, {
        method: "POST"
    })
    .then(res => {
        if (!res.ok) throw new Error("Vacate failed");
        return res.text();
    })
    .then(() => {
        closeVacateModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}

function closeVacateModal() {
    document.getElementById("vacateModal").style.display = "none";
}



//open student model

let currentSeatNumber = null;
let currentStudentId = null;

function openStudentModal(seatNumber) {
    currentSeatNumber = seatNumber;

    fetch(`/api/student/seat/${seatNumber}`)
        .then(res => {
            if (!res.ok) throw new Error("Student not found");
            return res.json();
        })
        .then(student => {
            currentStudentId = student.id;

            document.getElementById("detailSeatNo").innerText = seatNumber;
            document.getElementById("detailName").value = student.name;
            document.getElementById("detailPhone").value = student.phone;
            document.getElementById("detailJoinDate").value = student.bookingDate;
            document.getElementById("detailExpireDate").value = student.expiryDate;
            document.getElementById("detailAmount").value = student.amountPaid;

            document.getElementById("studentModal").style.display = "block";
        })
        .catch(err => alert(err.message));
}

function closeStudentModal() {
    document.getElementById("studentModal").style.display = "none";
}


// Vacate Button (Reuse Existing API)
function vacateSeat() {
    fetch(`/api/vacate/${currentSeatNumber}`, { method: "POST" })
        .then(() => {
            closeStudentModal();
            refreshUI();
        });
}


//update button
function updateStudent() {

    const payload = {
        name: document.getElementById("detailName").value,
        phone: document.getElementById("detailPhone").value,
        amountPaid: document.getElementById("detailAmount").value,
        expireDate: document.getElementById("detailExpireDate").value
    };

    fetch(`/api/student/${currentStudentId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Update failed");
        return res.text();
    })
    .then(() => {
        alert("Updated successfully");
        closeStudentModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}

//when dashboard page load
loadExpiryNotifications();

function loadExpiryNotifications() {
  fetch("/api/student/expiring-soon")
    .then(res => res.json())
    .then(renderExpiryList);
}

//
//data.forEach(s => {
//  const tr = document.createElement("tr");
// tr.innerHTML = `
//     <td>${s.seatNumber}</td>
//     <td>${s.name}</td>
//     <td>${s.phone}</td>
//     <td>${s.expireDate ? s.expireDate : "-"}</td>
//     <td>₹${s.amountPaid}</td>
//     <td>
//       <button onclick="editStudent(${s.id}">Edit</button>
//       <button onclick="sendReminder('${s.phone}', '${s.name}', ${s.seatNumber}, '${s.expiryDate}')">Send</button>
//     </td>
//  `;

function renderExpiryList(data) {
  const box = document.getElementById("expiryBody");
  box.innerHTML = "";

//  data.forEach(s => {
//    const row = document.createElement("div");
//    row.innerHTML = `
//      <b>${s.name}</b> (Seat ${s.seatNumber})<br>
//      ${s.phone} | Expiry: ${s.expireDate}
//      <br>
//      <button onclick="editStudent(${s.id})">Edit</button>
//      <button onclick="sendReminder('${s.phone}', '${s.name}', ${s.seatNumber}, '${s.expiryDate}')">
//        Send
//      </button>

    data.forEach(s => {
      const tr = document.createElement("tr");
     tr.innerHTML = `
         <td>${s.seatNumber}</td>
         <td>${s.name}</td>
         <td>${s.phone}</td>
         <td>${s.expireDate ? s.expireDate : "-"}</td>
         <td>₹${s.amountPaid}</td>
         <td>
           <button onclick="editStudent(${s.id}">Edit</button>
           <button onclick="sendReminder('${s.phone}', '${s.name}', ${s.seatNumber}, '${s.expireDate}')">Send</button>
         </td>
    `;
    box.appendChild(tr);
  });
}

function sendReminder(phone, name, seat, expiry) {
  const msg = `
Hello ${name} 👋

Your library seat (Seat No: ${seat})
is expiring on ${expiry}.

Please renew to continue your seat.
Thank you 🙏
`;

  window.open(
    `https://wa.me/91${phone}?text=${encodeURIComponent(msg)}`,
    "_blank"
  );
}



//forcing to close the student object again and again
window.onload = () => {
    document.getElementById("bookingModal").style.display = "none";
    document.getElementById("studentModal").style.display = "none";
};


