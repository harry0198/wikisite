const errorClass = 'error-form';


function isLink(str) {
    var pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
        '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
        '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
        '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
        '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
        '(\\#[-a-z\\d_]*)?$','i'); // fragment locator
    return !!pattern.test(str);
}

function isEmail(input) {

    var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    return !!input.value.match(validRegex);

}

function validationFailed(element, errorMsg) {
    if (element.nextSibling?.classList?.contains(errorClass)) return;
    let errorElement = document.createElement("p");

    errorElement.innerText = errorMsg;
    errorElement.classList.add(errorClass);

    element.classList.add(errorClass);
    element.after(errorElement);
}

function validationPassed(element) {
    let errorElement = element.nextSibling;
    if (element.nextSibling?.classList?.contains(errorClass)) {
        errorElement.remove();
        element.classList.remove(errorClass);
    }
}

export {
    isLink,
    validationPassed,
    validationFailed,
    isEmail
}