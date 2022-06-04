const FETCH_URL = "http://localhost:8080";

function getData(url, cb, data) {
    return submitRequest(url, cb, data,'GET');
}
function postData(url, cb, data) {
    return submitRequest(url, cb, data,'POST');
}
function patchData(url, cb, data) {
    return submitRequest(url, cb, data,'PATCH');
}
function deleteData(url, cb, data) {
    return submitRequest(url, cb, data, 'DELETE');
}

function submitRequest(url, cb, data, type) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        alert('Giving up :( Cannot create an XMLHTTP instance. Some functions may not work as expected.');
        return false;
    }
    httpRequest.open(type.toUpperCase(), `${FETCH_URL}${url}`, true);
    httpRequest.setRequestHeader(header, token);

    httpRequest.onreadystatechange = function() {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            cb(httpRequest.status);
        }
    };

    httpRequest.send(data);
    return httpRequest;
}

export {
    getData,
    postData,
    patchData,
    deleteData
}