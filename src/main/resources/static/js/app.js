
const API = {
  quotes: "/api/v1/quotes",
  prices: "/api/v1/analytics/prices",
  history: "/api/v1/analytics/prices/history",
  variation: "/api/v1/analytics/prices/variation"
};

const $ = (id) => document.getElementById(id);

const formatBRL = (value) => {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  return Number(value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
};

const formatNumber = (value) => {
  if (value === null || value === undefined || value === "") {
    return "—";
  }

  return Number(value).toLocaleString("pt-BR", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
};

function formatDate(date) {
  if (!date) return "—";

  const parts = String(date).split("-");

  if (parts.length !== 3) return date;

  return `${parts[2]}/${parts[1]}/${parts[0]}`;
}

function getFilters() {
  return {
    commodity: $("commodity").value.trim(),
    startDate: $("startDate").value,
    endDate: $("endDate").value,
    unit: $("unit").value
  };
}

function buildUrl(endpoint, filters) {
  const params = new URLSearchParams(filters);
  return `${endpoint}?${params.toString()}`;
}

async function getJSON(url) {
  const response = await fetch(url, {
    headers: {
      Accept: "application/json"
    }
  });

  if (!response.ok) {
    throw new Error(`Erro HTTP ${response.status}`);
  }

  return response.json();
}

function setFeedback(message, isError = false) {
  $("feedback").textContent = message;
  $("feedback").classList.toggle("error", isError);
}

function renderMetrics(analytics, variation) {
  $("averagePrice").textContent = formatBRL(analytics.averagePrice);
  $("minimumPrice").textContent = formatBRL(analytics.minimumPrice);
  $("maximumPrice").textContent = formatBRL(analytics.maximumPrice);

  const percentage = Number(variation.percentageVariation || 0);

  $("variation").textContent =
    `${percentage > 0 ? "+" : ""}${formatNumber(percentage)}%`;

  $("variation").style.color =
    percentage < 0 ? "#bd5148" :
    percentage > 0 ? "#2d714c" : "";

  const trend = variation.trend || "ESTÁVEL";

  $("variationCaption").textContent =
    trend === "ALTA" ? "Alta no período" :
    trend === "QUEDA" ? "Queda no período" :
    "Sem variação no período";

  $("initialPrice").textContent = formatBRL(variation.initialPrice);
  $("finalPrice").textContent = formatBRL(variation.finalPrice);

  $("initialDate").textContent = formatDate(variation.initialDate);
  $("finalDate").textContent = formatDate(variation.finalDate);

  const trendElement = $("trend");

  trendElement.textContent =
    trend === "ALTA" ? "↑ Alta" :
    trend === "QUEDA" ? "↓ Queda" :
    "→ Estável";

  trendElement.classList.toggle("down", trend === "QUEDA");

  const absoluteVariation = Number(variation.absoluteVariation || 0);

  $("absoluteVariation").textContent =
    `Variação: ${absoluteVariation > 0 ? "+" : ""}${formatBRL(absoluteVariation)}`;

  $("chartUnit").textContent = variation.unit || "Unidade selecionada";
}

function renderHistory(history) {
  const container = $("chartContainer");
  const points = Array.isArray(history.history) ? history.history : [];

  $("historyCount").textContent = `${points.length} dias com cotações`;

  if (points.length === 0) {
    container.innerHTML =
      '<div class="empty-state">Não há histórico para o período selecionado.</div>';
    return;
  }

  const width = 720;
  const height = 270;
  const left = 65;
  const right = 20;
  const top = 24;
  const bottom = 40;

  const prices = points.map((point) => Number(point.averagePrice));
  const minPrice = Math.min(...prices);
  const maxPrice = Math.max(...prices);
  const priceRange = maxPrice - minPrice || 1;

  function x(index) {
    if (points.length === 1) {
      return (width - left - right) / 2 + left;
    }

    return left +
      index * (width - left - right) / (points.length - 1);
  }

  function y(price) {
    return top +
      ((maxPrice - price) / priceRange) * (height - top - bottom);
  }

  const gridLines = Array.from({ length: 5 }, (_, index) => {
    const gridY = top +
      index * (height - top - bottom) / 4;

    const value = maxPrice - index * priceRange / 4;

    return `
      <line
        x1="${left}"
        y1="${gridY}"
        x2="${width - right}"
        y2="${gridY}"
        stroke="#e8eee8"
        stroke-dasharray="4 5"
      />
      <text
        x="${left - 10}"
        y="${gridY + 4}"
        text-anchor="end"
        font-size="11"
        fill="#7b887f"
      >${formatNumber(value)}</text>
    `;
  }).join("");

  const linePath = points.map((point, index) => {
    const command = index === 0 ? "M" : "L";
    const pointX = x(index);
    const pointY = y(Number(point.averagePrice));

    return `${command} ${pointX} ${pointY}`;
  }).join(" ");

  const areaPath =
    `${linePath} ` +
    `L ${x(points.length - 1)} ${height - bottom} ` +
    `L ${x(0)} ${height - bottom} Z`;

  const circles = points.map((point, index) => {
    const pointX = x(index);
    const pointY = y(Number(point.averagePrice));

    return `
      <circle
        cx="${pointX}"
        cy="${pointY}"
        r="4"
        fill="#2d714c"
        stroke="#ffffff"
        stroke-width="2"
      >
        <title>${formatDate(point.date)} — ${formatBRL(point.averagePrice)}</title>
      </circle>
    `;
  }).join("");

  const labels = points.map((point, index) => {
    const shouldShow =
      points.length <= 7 ||
      index % 2 === 0 ||
      index === points.length - 1;

    if (!shouldShow) return "";

    return `
      <text
        x="${x(index)}"
        y="${height - 12}"
        text-anchor="middle"
        font-size="10"
        fill="#7b887f"
      >${formatDate(point.date).slice(0, 5)}</text>
    `;
  }).join("");

  container.innerHTML = `
    <svg
      viewBox="0 0 ${width} ${height}"
      role="img"
      aria-label="Evolução histórica dos preços agrícolas"
    >
      <defs>
        <linearGradient id="chartFill" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stop-color="#8db99a" stop-opacity="0.34"/>
          <stop offset="100%" stop-color="#8db99a" stop-opacity="0"/>
        </linearGradient>
      </defs>

      ${gridLines}

      <path
        d="${areaPath}"
        fill="url(#chartFill)"
      />

      <path
        d="${linePath}"
        fill="none"
        stroke="#2d714c"
        stroke-width="2.5"
        stroke-linecap="round"
        stroke-linejoin="round"
      />

      ${circles}
      ${labels}
    </svg>
  `;
}

function escapeHTML(value) {
  return String(value ?? "").replace(/[&<>"']/g, (character) => {
    const entities = {
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#39;"
    };

    return entities[character];
  });
}

function renderQuotes(quotes, filters) {
  const tbody = $("quotesTable");

  const selected = quotes.filter((quote) => {
    const sameCommodity =
      String(quote.commodity || "").toLowerCase() ===
      filters.commodity.toLowerCase();

    const date = String(quote.referenceDate || "");

    const inRange =
      date >= filters.startDate &&
      date <= filters.endDate;

    const sameUnit =
      String(quote.unit || "").toLowerCase() ===
      filters.unit.toLowerCase();

    return sameCommodity && inRange && sameUnit;
  });

  selected.sort((a, b) =>
    String(b.referenceDate).localeCompare(String(a.referenceDate))
  );

  const recentQuotes = selected.slice(0, 10);

  $("quotesCount").textContent = `${selected.length} cotações`;

  if (recentQuotes.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="6" class="table-empty">
          Nenhuma cotação encontrada para os filtros selecionados.
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = recentQuotes.map((quote) => `
    <tr>
      <td class="crop-name">${escapeHTML(quote.commodity || "—")}</td>
      <td>
        ${escapeHTML(quote.region || "—")}${quote.state ? `/${escapeHTML(quote.state)}` : ""}
      </td>
      <td>${formatDate(quote.referenceDate)}</td>
      <td class="price-cell">${formatBRL(quote.price)}</td>
      <td>${escapeHTML(quote.unit || "—")}</td>
      <td>${escapeHTML(quote.source || "—")}</td>
    </tr>
  `).join("");
}

async function loadDashboard() {
  const filters = getFilters();

  if (!filters.startDate || !filters.endDate) {
    setFeedback("Informe a data inicial e a data final.", true);
    return;
  }

  if (filters.startDate > filters.endDate) {
    setFeedback("A data inicial não pode ser posterior à data final.", true);
    return;
  }

  setFeedback("Consultando a API AgroMarket...");

  $("applyFilters").disabled = true;
  $("refreshButton").disabled = true;

  try {
    const [analytics, history, variation, quotes] = await Promise.all([
      getJSON(buildUrl(API.prices, filters)),
      getJSON(buildUrl(API.history, filters)),
      getJSON(buildUrl(API.variation, filters)),
      getJSON(API.quotes)
    ]);

    renderMetrics(analytics, variation);
    renderHistory(history);
    renderQuotes(quotes, filters);

    setFeedback(
      `Dados atualizados. ${analytics.quantity ?? 0} cotações consideradas na análise.`
    );
  } catch (error) {
    console.error("Erro ao carregar o AgroMarket:", error);

    setFeedback(
      `Não foi possível carregar os dados: ${error.message}. Verifique a API e os filtros.`,
      true
    );
  } finally {
    $("applyFilters").disabled = false;
    $("refreshButton").disabled = false;
  }
}

$("applyFilters").addEventListener("click", loadDashboard);
$("refreshButton").addEventListener("click", loadDashboard);

$("commodity").addEventListener("change", () => {
  if ($("commodity").value.toLowerCase() === "soja") {
    $("unit").value = "BRL/sc60kg";
  } else {
    $("unit").value = "saca de 60 kg";
  }
});

loadDashboard();
