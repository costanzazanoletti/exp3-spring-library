/* GLOBAL VARIABLES */
const BOOKS_URL = 'http://localhost:8080/api/v1/books';
const contentElement = document.getElementById('content');
const toggleForm = document.getElementById('toggle-form');
const bookForm = document.getElementById('book-form');

/* API */
const getBookList = async () => {
  const response = await fetch(BOOKS_URL);
  return response;
};

const postBook = async (jsonData) => {
  const fetchOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: jsonData,
  };
  const response = await fetch(BOOKS_URL, fetchOptions);
  return response;
};

const deleteBookById = async (bookId) => {
  const response = await fetch(BOOKS_URL + '/' + bookId, { method: 'DELETE' });
  return response;
};
/* DOM MANIPULATION */
const toggleFormVisibility = () => {
  document.getElementById('form').classList.toggle('d-none');
};

const createCategoryList = (categories) => {
  let categoriesHtml = '<p>';
  categories.forEach((el) => {
    categoriesHtml += `<span class="mx-1 badge text-bg-info">${el.name}</span>`;
  });
  categoriesHtml += '</p>';
  return categoriesHtml;
};
const createDeleteBtn = (book) => {
  let btn = '';
  const borrowings = book.borrowings;
  if (borrowings !== null && borrowings.length > 0) {
    // disabled button
    btn = `<button class="btn btn-danger" disabled>
            Delete
        </button>`;
  } else {
    btn = `<button data-id="${book.id}" class="btn btn-danger">
            Delete
        </button>`;
  }
  return btn;
};
const createBookItem = (item) => {
  return `<div class="col-4">
            <div class="card h-100">
                <div class="card-header">isbn: ${item.isbn}</div>
                <div class="card-body">
                    <h5 class="card-title">${item.title}</h5>
                    <h6 class="card-subtitle mb-2 text-body-secondary">${
                      item.authors
                    }</h6>
                    <p class="card-text d-flex justify-content-between">
                        <span>${item.publisher}</span>
                        <span>${item.year}</span>
                    </p>
                    <p class="fw-light">${
                      item.synopsis ? item.synopsis : 'Synopsis not available'
                    }</p>
                    <p>${item.numberOfCopies} copies (${
    item.availableCopies
  } available)</p>
                    <div>${createDeleteBtn(item)}</div>
                </div>
                <div class="card-footer">
                    ${createCategoryList(item.categories)}
                </div>
            </div>
    </div>`;
};

const createBookList = (data) => {
  console.log(data);
  let list = `<div class="row gy-3">`;
  // add book items
  data.forEach((element) => {
    list += createBookItem(element);
  });
  list += '</div>';
  return list;
};
/* OTHER FUNCTIONS */
const loadData = async () => {
  // call api
  const response = await getBookList();
  console.log(response);
  if (response.ok) {
    // get data
    const data = await response.json();
    // render html
    contentElement.innerHTML = createBookList(data);
    // add event listeners
    const deleteBtns = document.querySelectorAll('button[data-id]');
    console.log(deleteBtns);
    for (let btn of deleteBtns) {
      btn.addEventListener('click', () => {
        deleteBook(btn.dataset.id);
      });
    }
  } else {
    // handle error
    console.log('ERROR');
  }
};
const saveBook = async (event) => {
  // prevent default
  event.preventDefault();
  // read input values
  const formData = new FormData(event.target);
  const formDataObj = Object.fromEntries(formData.entries());
  // VALIDATION
  // produce a json
  const formDataJson = JSON.stringify(formDataObj);
  // send ajax request
  const response = await postBook(formDataJson);
  // parse response
  const responseBody = await response.json();
  if (response.ok) {
    // reload data
    loadData();
    // reset form
    event.target.reset();
  } else {
    // handle error
    console.log('ERROR');
    console.log(response.status);
    console.log(responseBody);
  }
};
const deleteBook = async (bookId) => {
  console.log('delete book ' + bookId);
  // call delete api
  const response = await deleteBookById(bookId);
  // parse response
  if (response.ok) {
    // reload book list
    loadData();
  } else {
    // handle error
    console.log('ERROR');
    console.log(response.status);
  }
};

/* GLOBAL CODE */
toggleForm.addEventListener('click', toggleFormVisibility);
bookForm.addEventListener('submit', saveBook);
loadData();
