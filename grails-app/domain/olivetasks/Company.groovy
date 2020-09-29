package olivetasks

class Company {

    String companyName
    //String phone
    //String email

    //byte[] logo
    String logo

    Company(String companyName, String logo) {
        this.companyName = companyName
        this.logo = logo
    }
//static hasMany = [addresses: Address]

    static constraints = {
        companyName nullable: false, blank: false
        //logo nullable:true, maxSize: 400 * 400 * 2
        logo nullable:true

    }

    @Override
    String toString() {
        return companyName
    }
}
