package olivetasks

class Address {

    String street
    String city
    String state
    String postalCode
    String country

    //static belongsTo = [company: Company]

    static constraints = {
        country(nullable: false)
    }

    @Override
    String toString() {
        return street + '\n' + city + '\n' + state + '\n' + postalCode + '\n' + country
    }
}
