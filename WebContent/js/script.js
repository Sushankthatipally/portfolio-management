/* ===== Portfolio Management System Core Scripts =====
   Demonstrates DOM, event handlers (keyboard, mouse, window),
   arrays, array functions, string objects, Date object, AJAX (XML & JSON). */

// Window event - page load
window.addEventListener('load', function () {
    console.log('Portfolio Management System loaded at ' + new Date().toLocaleString());
    // Attach fade-in animation to panels
    const panels = document.querySelectorAll('.panel, .card');
    panels.forEach(function (p, i) {
        setTimeout(function () { p.classList.add('fade-in'); }, i * 40);
    });
});

/* ===== Keyboard Event Handler =====
   Press '/' anywhere to focus a search box on the page. */
document.addEventListener('keydown', function (e) {
    if (e.key === '/' && document.activeElement.tagName !== 'INPUT') {
        const search = document.querySelector('input[name="search"], #liveSearch');
        if (search) {
            e.preventDefault();
            search.focus();
        }
    }
});

/* ===== Live table filter (DOM + array functions + strings) ===== */
function liveFilter(inputId, tbodyId) {
    const input = document.getElementById(inputId);
    const tbody = document.getElementById(tbodyId);
    if (!input || !tbody) return;

    const query = input.value.toLowerCase().trim();
    const rows = Array.from(tbody.getElementsByTagName('tr'));

    rows.forEach(function (row) {
        const text = row.textContent.toLowerCase();
        row.style.display = text.indexOf(query) !== -1 ? '' : 'none';
    });
}

/* ===== AJAX: load stocks from servlet as XML and render =====
   Demonstrates: XMLHttpRequest, XML DOM parsing, dynamic HTML. */
function loadStocksAjax() {
    const status = document.getElementById('ajaxStatus');
    const body   = document.getElementById('marketBody');
    if (!body) return;
    if (status) status.textContent = 'Loading via AJAX (XML)...';

    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'api/stocks', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) return;
        if (xhr.status === 200) {
            const xml = xhr.responseXML;
            renderStocksFromXML(xml, body);
            if (status) status.textContent = 'Updated ' + new Date().toLocaleTimeString();
        } else {
            if (status) status.textContent = 'Error loading data: ' + xhr.status;
        }
    };
    xhr.send();
}

function renderStocksFromXML(xml, body) {
    const stocks = xml.getElementsByTagName('stock');
    let html = '';
    for (let i = 0; i < stocks.length; i++) {
        const s = stocks[i];
        const getVal = function (tag) {
            const el = s.getElementsByTagName(tag)[0];
            return el ? el.textContent : '';
        };
        const stockId       = getVal('stockId');
        const symbol        = getVal('symbol');
        const company       = getVal('companyName');
        const sector        = getVal('sector');
        const price         = parseFloat(getVal('currentPrice'));
        const prevClose     = parseFloat(getVal('previousClose'));
        const change        = parseFloat(getVal('change'));
        const changePct     = parseFloat(getVal('changePercent'));
        const cls           = change >= 0 ? 'positive' : 'negative';
        const sign          = change >= 0 ? '+' : '';

        html += '<tr>' +
            '<td><strong>' + symbol + '</strong></td>' +
            '<td>' + company + '</td>' +
            '<td>' + sector + '</td>' +
            '<td class="num">&#8377;' + price.toFixed(2) + '</td>' +
            '<td class="num">&#8377;' + prevClose.toFixed(2) + '</td>' +
            '<td class="num ' + cls + '">' + sign + change.toFixed(2) + ' (' + sign + changePct.toFixed(2) + '%)</td>' +
            '<td>-</td>' +
            '<td>' +
                '<a class="btn btn-sm btn-success" href="trade?stockId=' + stockId + '&action=BUY">Buy</a> ' +
                '<a class="btn btn-sm btn-danger" href="trade?stockId=' + stockId + '&action=SELL">Sell</a>' +
            '</td>' +
        '</tr>';
    }
    body.innerHTML = html || '<tr><td colspan="8" class="empty">No stocks returned.</td></tr>';
}

/* ===== Mouse event: row hover highlights (delegation) ===== */
document.addEventListener('mouseover', function (e) {
    const row = e.target.closest && e.target.closest('tbody tr');
    if (row) row.style.cursor = 'default';
});

/* ===== Confirm risky actions (form event) ===== */
document.addEventListener('submit', function (e) {
    const form = e.target;
    if (form && form.id === 'tradeForm') {
        const action = (form.querySelector('input[name="action"]') || {}).value;
        if (action === 'SELL' && !confirm('Confirm sell order?')) {
            e.preventDefault();
        }
    }
});

/* ===== Sample arrays/strings/Date demo: greeting in console ===== */
(function () {
    const greetings = ['Welcome', 'Glad to see you', 'Hello'];
    const pick = greetings[Math.floor(Math.random() * greetings.length)];
    const now = new Date();
    console.log(pick + ' — ' + now.toDateString());
}());
