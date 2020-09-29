package olivetasks

import grails.gorm.transactions.Transactional
import grails.web.context.ServletContextHolder
import org.springframework.web.multipart.MultipartFile

@Transactional
class FileUploadService {

    //TODO: use custom destination directory in future
    boolean transactional = true

    def String uploadFile(MultipartFile file, String name) {

        def servletContext = ServletContextHolder.servletContext
        def storagePath = servletContext.getRealPath('/assets')  //destinationDirectory

        // Create storage path directory if it does not exist
        def storagePathDirectory = new File(storagePath)
        if (!storagePathDirectory.exists()) {
            print "CREATING DIRECTORY ${storagePath}: "
            if (storagePathDirectory.mkdirs()) {
                println "SUCCESS"
            } else {
                println "FAILED"
            }
        }

        println("Saving file")

        // Store file
        if (!file.isEmpty()) {
            file.transferTo(new File("${storagePath}/${name}"))
            println "Saved file: ${storagePath}/${name}"
            return "${storagePath}/${name}"

        } else {
            println "File ${file.inspect()} was empty!"
            return null
        }
    }
}
