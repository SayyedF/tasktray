<a class="dropdown-item d-flex align-items-center" href="${alert?.url}" xmlns:g="http://www.w3.org/1999/XSL/Transform">
    <div class="mr-3">
        <div class="${alert?.divClass}">
            <i class="${alert?.iconClass}"></i>
        </div>
    </div>
    <div>
        <div class="small text-gray-500">${alert?.date}</div>
        <g:if test="${alert?.seen}">
            <span>${alert?.alert}</span>
        </g:if>
        <g:if test="${!alert?.seen}">
            <span class="${alert?.fontClass}">${alert?.alert}</span>
        </g:if>
    </div>
</a>