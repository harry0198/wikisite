import {init as commons, onLoad} from "./common.mjs";
import {applyDropdownEventHandlers} from "../modules/dropdown.mjs";

onLoad(() => {
    commons()
    applyDropdownEventHandlers()
});