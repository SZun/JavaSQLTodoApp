const baseURL = "http://localhost:8080/api/todo";

const displayAllItems = (arr) => {
  arr.forEach(({ id, name, description, startDate, endDate, finished }) => {
    $("#table__body").append(`
        <tr>
            <td>${name}</td>
            <td>${description == null ? "N/A" : description}</td>
            <td>${startDate}</td>
            <td>${endDate == null ? "N/A" : endDate}</td>
            <td>${!finished ? "Not " : ""}Finished</td>
            <td><a class="text-info" href="#" onclick="getById(${id})">Edit</a></td>
            <td><a class="text-info" href="#" onclick="deleteById(${id})">Delete</a></td>
        </tr>
        `);
  });
};

const setFormVals = todo => {
    let { id, name, description, startDate, endDate, finished } = todo;
    const todayISO = new Date().toISOString().split("").slice(0,10).join("");

    $("#todo").val(name);
    $("#start__date").val(startDate);
    $("#end__date").val(endDate == null ? "" : endDate);
    $("#description").val(description == null ? "" : description);
    $("#finished").val(finished);
    $("#finished").attr("checked", finished);
}

const getById = async (id) => {
  try {
    const toEdit = await $.ajax({ type: "GET", url: `${baseURL}/${id}` });
    setFormVals(toEdit);
  } catch (ex) {
    console.log({ Errors: ex });
  }
};

const deleteById = async (id) => {
  try {
    await $.ajax({ type: "DELETE", url: `${baseURL}/${id}` });
    getAll();
  } catch (ex) {
    console.log({ Errors: ex });
  }
};

const getAll = async () => {
  $("#table__body").empty();
  try {
    const allTodos = await $.ajax({ type: "GET", url: `${baseURL}s` });
    displayAllItems(allTodos);
  } catch (ex) {
    console.log({ Errors: ex });
  }
};

const main = () => {
  getAll();
  console.log()
};

main();
