/* ===== Form Validation =====
   Demonstrates: variables (var/let/const), operators, conditionals,
   functions, regular expressions, event handlers, DOM manipulation. */

function showError(id, message) {
    const el = document.getElementById(id);
    if (el) el.textContent = message;
}

function clearError(id) {
    showError(id, '');
}

/* ----- LOGIN form ----- */
function validateLogin() {
    let valid = true;
    clearError('usernameError');
    clearError('passwordError');

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (username.length < 3) {
        showError('usernameError', 'Username must be at least 3 characters');
        valid = false;
    }
    if (password.length < 6) {
        showError('passwordError', 'Password must be at least 6 characters');
        valid = false;
    }
    return valid;
}

/* ----- REGISTER form ----- */
function validateRegister() {
    let valid = true;
    const ids = ['fullNameError','usernameError','emailError','phoneError','passwordError','confirmError'];
    ids.forEach(clearError);

    const fullName = document.getElementById('fullName').value.trim();
    const username = document.getElementById('username').value.trim();
    const email    = document.getElementById('email').value.trim();
    const phone    = document.getElementById('phone').value.trim();
    const password = document.getElementById('password').value;
    const confirm  = document.getElementById('confirmPassword').value;

    // Regular Expression patterns
    const nameRegex     = /^[A-Za-z\s]{2,50}$/;
    const usernameRegex = /^[A-Za-z0-9_]{3,20}$/;
    const emailRegex    = /^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    const phoneRegex    = /^[0-9]{10}$/;
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

    if (!nameRegex.test(fullName)) {
        showError('fullNameError', 'Enter a valid full name (letters only)');
        valid = false;
    }
    if (!usernameRegex.test(username)) {
        showError('usernameError', 'Username: 3-20 chars, letters/digits/_ only');
        valid = false;
    }
    if (!emailRegex.test(email)) {
        showError('emailError', 'Enter a valid email address');
        valid = false;
    }
    if (phone && !phoneRegex.test(phone)) {
        showError('phoneError', 'Phone must be 10 digits');
        valid = false;
    }
    if (!passwordRegex.test(password)) {
        showError('passwordError', 'Password: min 6 chars with letters & digits');
        valid = false;
    }
    if (password !== confirm) {
        showError('confirmError', 'Passwords do not match');
        valid = false;
    }
    return valid;
}

/* Password strength meter — uses DOM style manipulation */
function checkPasswordStrength(pwd) {
    const meter = document.getElementById('passwordStrength');
    if (!meter) return;
    let score = 0;
    if (pwd.length >= 6) score++;
    if (pwd.length >= 10) score++;
    if (/[A-Z]/.test(pwd)) score++;
    if (/[0-9]/.test(pwd)) score++;
    if (/[^A-Za-z0-9]/.test(pwd)) score++;

    const levels = [
        { pct: '0%',   color: '#dc2626' },
        { pct: '25%',  color: '#dc2626' },
        { pct: '50%',  color: '#d97706' },
        { pct: '75%',  color: '#1f6feb' },
        { pct: '90%',  color: '#16a34a' },
        { pct: '100%', color: '#16a34a' }
    ];
    const lvl = levels[score];
    meter.style.setProperty('--strength', lvl.pct);
    meter.style.setProperty('--strength-color', lvl.color);
}

/* Live username availability — AJAX (XMLHttpRequest) */
function checkUsername(username) {
    if (!username || username.length < 3) return;
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'api/stocks?format=json', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Demo: just confirm endpoint reachable
        }
    };
    xhr.send();
}

/* ----- TRADE form ----- */
function validateTrade(maxQty) {
    clearError('quantityError');
    const qty = parseInt(document.getElementById('quantity').value, 10);
    if (isNaN(qty) || qty <= 0) {
        showError('quantityError', 'Enter a positive quantity');
        return false;
    }
    if (qty > maxQty) {
        showError('quantityError', 'Exceeds maximum allowed: ' + maxQty);
        return false;
    }
    return true;
}

function updateTotal(price) {
    const qty = parseInt(document.getElementById('quantity').value, 10) || 0;
    const total = qty * price;
    const out = document.getElementById('estTotal');
    if (out) out.innerHTML = '&#8377; ' + total.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}
