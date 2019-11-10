import setuptools

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="dynamodb_streaming_lib",
    version="0.0.2",
    author="Kenan Berbic",
    author_email="berbic_16@hotmail.com",
    description="Add PySpark support for reading AWS DynamoDB streams",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://bitbucket.org/kenanberbic/apache.spark.dynamodb.streaming",
    packages=setuptools.find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
    include_package_data=True,
    python_requires='>=3.6',
)
