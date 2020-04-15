module.exports = function(grunt) {
    grunt.initConfig({
        // Metadata
        meta: {
            srcPath: 'src/main/resources/sass/',
            resourcesPublicPath: 'src/main/resources/public/',
        },

        sass: {
            dist: {
                options: {
                    style: "expanded",
                },
                files: {
                    "<%= meta.resourcesPublicPath %>style.css": "<%= meta.srcPath %>main.scss"
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
        },

        copy: {
            main: {
                files: [
                    // material icons
                    {
                        expand: true,
                        cwd: 'node_modules/material-icons/iconfont',
                        src: [
                            '*.css',
                            'MaterialIcons-Regular*'
                        ],
                        dest: '<%= meta.resourcesPublicPath %>/vendor/material-icons'
                    },
                ]
            }
        }
    });

    grunt.loadNpmTasks("grunt-contrib-sass");
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-copy');

    grunt.registerTask('default', ['dev', 'watch']);
    grunt.registerTask('dev', ['sass'])
};