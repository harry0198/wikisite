import {BASE_URL} from "./util.mjs";
import Toast from "./toast.mjs";

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
        Toast("Could not submit request", "fa-circle-exclamation");
        console.log("Failed to create XMLHttpRequest!");
        return false;
    }
    httpRequest.open(type.toUpperCase(), `${BASE_URL}${url}`, true);
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