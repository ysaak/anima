module.exports = function(grunt) {
    grunt.initConfig({
        // Metadata
        meta: {
            srcPath: 'src/main/resources/sass/',
            deployPath: 'src/main/resources/public/'
        },

        sass: {
            dist: {
                options: {
                    style: "expanded",
                },
                files: {
                    "<%= meta.deployPath %>style.css": "<%= meta.srcPath %>main.scss"
                },
            },
        },

        watch: {
            scripts: {
                files: [
                    '<%= meta.srcPath %>/**/*.scss'
                ],
                tasks: ['sass']
            }
        }
    });

    grunt.loadNpmTasks("grunt-contrib-sass");
    grunt.loadNpmTasks('grunt-contrib-watch')

    grunt.registerTask('default', ['dev', 'watch']);
    grunt.registerTask('dev', ['sass'])
};