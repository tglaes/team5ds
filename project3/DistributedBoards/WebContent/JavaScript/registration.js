/**
 * 
 */
function checkFormData() {

            hideError()

            var isValid = true

            var firstname = document.getElementById("vorname").value;
            var lastname = document.getElementById("nachname").value;
            var email = document.getElementById("email").value;
            var password = document.getElementById("password").value;
            var username = document.getElementById("benutzername").value;
            var profession = document.getElementById("beruf").value;
            var age = document.getElementById("alter").value;
			
            var result1 = checkFirstName(firstname)
            var result2 = checkLastName(lastname)
            var result3 = checkEmail(email)
            var result4 = checkPassword(password)
            var result5 = checkUsername(username)
            var result6 = checkProfession(profession)
            var result7 = checkAge(age)
                       
            if (result1 && result2 && result3 && result4 && result5 && result5 && result6 && result7) {
                document.getElementById("registrationForm").submit();
            } else {
                showError()
            }        
        }

        function checkFirstName(firstname) {
            if (firstname && !checkForWhiteSpace(firstname)) {
                var v = document.getElementById("vorname")
                v.className = "form-control is-valid"
                return true
            } 
            var v = document.getElementById("vorname")
            v.className = "form-control is-invalid"
            return false
        }

        function checkLastName(lastname) {
            if (lastname && !checkForWhiteSpace(lastname)) {
                var v = document.getElementById("nachname")
                v.className = "form-control is-valid"
                return true
            }
            var v = document.getElementById("nachname")
            v.className = "form-control is-invalid"
            return false
        }

        function checkEmail(email) {
            if (email && !checkForWhiteSpace(email)) {
                var re = /\S+@\S+\.\S+/;
                if (re.test(email)) {
                    var v = document.getElementById("email")
                    v.className = "form-control is-valid"
                    return true
                }
            }
            var v = document.getElementById("email")
            v.className = "form-control is-invalid"
            return false
        }

        function checkPassword(password) {
            if (password && !checkForWhiteSpace(password)) {
                var v = document.getElementById("password")
                v.className = "form-control is-valid"
                return true
            }
            var v = document.getElementById("password")
            v.className = "form-control is-invalid"
            return false
        }

        function checkUsername(username) {
            if (username && !checkForWhiteSpace(username)) {
                var v = document.getElementById("benutzername")
                v.className = "form-control is-valid"
                return true
            }
            var v = document.getElementById("benutzername")
            v.className = "form-control is-invalid"
            return false
        }

        function checkProfession(profession) {
            if (profession && !checkForWhiteSpace(profession)) {
                var v = document.getElementById("beruf")
                v.className = "form-control is-valid"
                return true
            }
            var v = document.getElementById("beruf")
            v.className = "form-control is-invalid"
            return false
        }

        function checkAge(age) {
            if (age && !checkForWhiteSpace(age)) {
                if (/^\d+$/.test(age)) {
                    var v = document.getElementById("alter")
                    v.className = "form-control is-valid"
                    return true
                }
            }
            var v = document.getElementById("alter")
            v.className = "form-control is-invalid"
            return false
        }

        function checkForWhiteSpace(string) {
            return string.indexOf(' ') >= 0;
        }

        function showError() {
            var errorDiv = document.getElementById("errorDiv")
            errorDiv.hidden = false
        }

        function hideError() {
            var errorDiv = document.getElementById("errorDiv")
            errorDiv.hidden = true
        }