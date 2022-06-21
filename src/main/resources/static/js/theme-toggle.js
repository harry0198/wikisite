const storageKey = 'theme-preference';
const systemKey = 'theme-system';

const onClick = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light';

    setPreference();
}

const getColorPreference = () => {
    if (localStorage.getItem(storageKey))
        return localStorage.getItem(storageKey);
    else
        return 'dark';
}

const setPreference = () => {
    localStorage.setItem(storageKey, theme.value)
    reflectPreference()
}

const reflectPreference = () => {
    document.firstElementChild.setAttribute('page-style', theme.value)
    // update preference btn
    for (let elementsByClassNameElement of document.getElementsByClassName('pagestyle')) {
        elementsByClassNameElement.addEventListener('click', () => {
            document.getElementById('pagestyle')?.setAttribute('aria-label', theme.value);
        });
    }
}

function clearColorPreference() {
    localStorage.setItem(systemKey, 'system');
    theme.value = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    setPreference()
}

function setDarkMode() {
    theme.value = 'dark';
    localStorage.setItem(storageKey, 'dark');
    localStorage.removeItem(systemKey);
    setPreference();
}

function setLightMode() {
    theme.value = 'light';
    localStorage.setItem(storageKey, 'light');
    localStorage.removeItem(systemKey);
    setPreference();
}


const theme = {
    value: getColorPreference(),
}

// Set early to prevent page flashes
reflectPreference();

window.onload = () => {
    reflectPreference()

    // query selector
    for (let elementsByClassNameElement of document.getElementsByClassName('pagestyle')) {
        elementsByClassNameElement.addEventListener('click', () => {
            onClick();
        });
    }
}

window
.matchMedia('(prefers-color-scheme: dark)')
.addEventListener('change', ({matches:isDark}) => {
    if (localStorage.getItem(systemKey) === 'system') {
        theme.value = isDark ? 'dark' : 'light'
        setPreference()
    }
});
export {
    clearColorPreference,
    setLightMode,
    setDarkMode
}