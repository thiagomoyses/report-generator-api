const reportNameInput = document.getElementById("reportName");
const formatInput = document.getElementById("format");
const generateButton = document.getElementById("generateButton");

const successMessage = document.getElementById("successMessage");
const errorMessage = document.getElementById("errorMessage");


// ============================================================
// JSON Editor
// ============================================================

const container = document.getElementById("parametersEditor");

const editor = new JSONEditor(container, {
    mode: "code",
    modes: ["code"],
    mainMenuBar: false,
    navigationBar: true,
    statusBar: true
});

editor.set({
    studentId: "123",
    year: "2026"
});


// ============================================================
// Generate report
// ============================================================

generateButton.addEventListener("click", async () => {

    hideMessages();

    let parameters;

    // Read JSON from editor
    try {

        parameters = editor.get();

    } catch (error) {

        showError(
            "Invalid JSON. Please check the parameters."
        );

        return;
    }


    const reportName = reportNameInput.value;
    const format = formatInput.value;


    // Disable button while generating
    generateButton.disabled = true;
    generateButton.textContent = "Generating...";


    try {

        const response = await fetch(
            `/reports/${reportName}/${format}`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(parameters)
            }
        );


        // API returned an error
        if (!response.ok) {

            throw new Error(
                `HTTP ${response.status}`
            );
        }


        // API returns the generated file
        const blob = await response.blob();


        // Create temporary URL for the file
        const url = window.URL.createObjectURL(blob);


        // Create temporary download link
        const link = document.createElement("a");

        link.href = url;

        link.download =
            `${reportName}.${format.toLowerCase()}`;


        document.body.appendChild(link);

        link.click();

        link.remove();


        // Release temporary URL
        window.URL.revokeObjectURL(url);


        showSuccess(
            "Report generated successfully."
        );

    } catch (error) {

        console.error(
            "Error generating report:",
            error
        );

        showError(
            "Unable to generate the report."
        );

    } finally {

        // Re-enable button
        generateButton.disabled = false;

        generateButton.textContent =
            "Generate report";
    }

});


// ============================================================
// Messages
// ============================================================

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