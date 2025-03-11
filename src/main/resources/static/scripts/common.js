function updateDishList(categorySelectId, dishSelectId, hiddenDishId) {
    let categoryName = document.getElementById(categorySelectId).value;

    if (!categoryName) {
        document.getElementById(dishSelectId).innerHTML = '<option value="" hidden>Wybierz danie</option>';
        return;
    }

    fetch('/admin/dishes?category=' + categoryName)
        .then(response => response.json())
        .then(dishes => {
            let dishSelect = document.getElementById(dishSelectId);
            dishSelect.innerHTML = ''; // Wyczyść poprzednie opcje

            let option = document.createElement('option');
            option.text = 'Wybierz danie';
            option.value = '';
            dishSelect.appendChild(option);

            dishes.forEach(dish => {
                let option = document.createElement('option');
                option.text = dish.name;  // Wyświetlana nazwa dania
                option.value = dish.id;   // ID dania jako wartość
                dishSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Błąd:', error);
        });
}

document.getElementById('dishCat').addEventListener('change', function () {
    updateDishList('dishCat', 'dish', 'dishId');
});

document.getElementById('dishCatUpdate').addEventListener('change', function () {
    updateDishList('dishCatUpdate', 'dishUpdate', 'dishIdUpdate');
});

document.getElementById('dish').addEventListener('change', function () {
    document.getElementById('dishId').value = this.value;
});

document.getElementById('dishUpdate').addEventListener('change', function () {
    let selectedDishId = this.value;
    document.getElementById('dishIdUpdate').value = this.value;
});
