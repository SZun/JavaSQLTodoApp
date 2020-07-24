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
            <td><a class="text-info" href="/edit">Edit</a></td>
            <td><a class="text-info" href="#" onclick="deleteById(${id})">Delete</a></td>
        </tr>
        `);
  });
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
    console.log(allTodos);
    displayAllItems(allTodos);
  } catch (ex) {
    console.log({ Errors: ex });
  }
};

const main = () => {
  getAll();
};

main();
