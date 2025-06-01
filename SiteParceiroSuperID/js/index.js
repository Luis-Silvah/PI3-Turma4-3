const PROJECT_ID = "superid-pi3-turma4-3";
const API_KEY =
  "y7QAANKBedPovC-wkOG_Pru_5cz-cbEilGQsS0wUINkfjqFBskZvumDrllOQ4R_V9LeXLqVs6lrDFUzwi35RUu0j1BgRjK4ozltngFdcG0sI7qo8-xMsin6EVXpUahD3_JNlXxSMyzapFZQCpnohk5rvUWkYFH0aLrqg_xhKJCmrPA3WRcmZz_jlGiEUM_Dz33e3QyEFPmk0RH7PIQdQej26mJZoM4H0H9iOHD6Bpsp-kzst0PHsWuFu_2oYR1OL";
const SITE_URL = "www.vercel.app";

let loginToken = null;
let pollInterval = null;
let pollCount = 0;
let modal;
const MAX_POLLS = 10;
const POLL_INTERVAL_MS = 3000;

function openModal() {
  const modalElement = document.getElementById("superIDModal");
  modal = new bootstrap.Modal(modalElement);
  modal.show();

  generateQRCode();
}

function closeModal() {
  if (modal) {
    clearInterval(pollInterval);
    modal.hide();
    document.getElementById("status").innerText = "Aguardando escaneamento...";
    document.getElementById("qrcode").src = "";
  }
}

async function generateQRCode() {
  try {
    const res = await fetch(
      `https://us-central1-super-id-a0d24.cloudfunctions.net/performAuth`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          apiKey: API_KEY,
          url: SITE_URL,
        }),
      }
    );

    const data = await res.json();
    loginToken = data.loginToken;

    document.getElementById("qrcode").src = data.qrBase64;
    document.getElementById("status").innerText = "Escaneie com o app SuperID";

    pollCount = 0;
    if (pollInterval) clearInterval(pollInterval);
    pollInterval = setInterval(checkLoginStatus, POLL_INTERVAL_MS);
  } catch (err) {
    document.getElementById("status").innerText = "Erro ao gerar QR Code";
    console.error(err);
  }
}

async function checkLoginStatus() {
  try {
    const res = await fetch(
      `https://us-central1-super-id-a0d24.cloudfunctions.net/getLoginStatus`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ loginToken }),
      }
    );

    const data = await res.json();

    if (data.status === "success") {
      clearInterval(pollInterval);
      showSuccessMessage();
    } else if (data.status === "expired") {
      clearInterval(pollInterval);
      document.getElementById("status").innerText =
        "Token expirado. Gerando novo QR Code...";
      setTimeout(generateQRCode, 1000);
    } else {
      pollCount++;
      if (pollCount >= MAX_POLLS) {
        clearInterval(pollInterval);
        document.getElementById("status").innerText =
          "Tempo esgotado. Gerando novo QR Code...";
        setTimeout(generateQRCode, 1000);
      }
    }
  } catch (err) {
    clearInterval(pollInterval);
    document.getElementById("status").innerText = "Erro ao verificar status";
    console.error(err);
  }
}

function showSuccessMessage(message = "✅ Login efetuado com sucesso!") {
  const snackbar = document.getElementById("snackbar");
  snackbar.innerText = message;
  snackbar.classList.add("show");

  setTimeout(() => {
    snackbar.classList.remove("show");
    closeModal();
  }, 3000);
}
