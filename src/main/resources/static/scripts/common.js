document.getElementById('dishCat').addEventListener('change', function () {
    let categoryName = this.value;

    if (!categoryName) {
        document.getElementById('dish').innerHTML = '<option value="" hidden>Wybierz danie</option>';
        return;
    }

    fetch('/admin/dishes?category=' + categoryName)
        .then(response => response.json())
        .then(dishes => {
            let dishSelect = document.getElementById('dish');
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
});

document.getElementById('dish').addEventListener('change', function () {
    const selectedDishId = this.value;
    document.getElementById('dishId').value = selectedDishId;
});