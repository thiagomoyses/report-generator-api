const reportNameInput = document.getElementById("reportName");
const formatInput = document.getElementById("format");
const generateButton = document.getElementById("generateButton");
const successMessage = document.getElementById("successMessage");
const errorMessage = document.getElementById("errorMessage");


// ---------------------------------------------------------
// JSON Editor
// ---------------------------------------------------------

const container = document.getElementById("parametersEditor");

const editor = new JSONEditor(container, {
    mode: "code",
    modes: ["code"],
    mainMenuBar: false,
    navigationBar: true,
    statusBar: true
});


// Default JSON
editor.set({
    studentId: "123",
    year: 2026
});


// ---------------------------------------------------------
// Generate
// ---------------------------------------------------------

generateButton.addEventListener("click", () => {

    hideMessages();
    let parameters;

    try {
        parameters = editor.get();
    } catch (error) {
        showError(
            "Invalid JSON. Type '{}' for empty params."
        );
        return;
    }


    const request = {
        reportName: reportNameInput.value,
        format: formatInput.value,
        parameters: parameters
    };

    console.log("Report request:");
    console.log(request);

    showSuccess(
        "JSON is valid."
    );
});


// ---------------------------------------------------------
// Messages
// ---------------------------------------------------------

function hideMessages() {

    successMessage.classList.add("d-none");
    errorMessage.classList.add("d-none");

}


function showSuccess(message) {

    successMessage.textContent = message;
    successMessage.classList.remove("d-none");

}


function showError(message) {

    errorMessage.textContent = message;
    errorMessage.classList.remove("d-none");

}